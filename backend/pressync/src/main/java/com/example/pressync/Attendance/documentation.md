# Attendance Module

## How it works
This module handles all operations related to user attendance at specific events.
It follows the CQRS (Command Query Responsibility Segregation) pattern,
separating read operations (QueryHandlers) from write operations (CommandHandler).
Operations are exposed via the `AttendanceController` and data is persisted using the `AttendanceRepository`.

## Why it is structured this way
The CQRS pattern is used here to decouple the read and write logic, which allows for better scalability,
easier maintenance, and clearer separation of concerns.
This is particularly useful as attendance logic might require different scaling profiles for read
(e.g. checking statistics) vs write (e.g. marking present).

## Connection to other components
- **User Module**: Associates attendance records with specific users.
- **Event Module**: Links attendance records to specific events that are occurring.
- **EventCategory Module**: Connects to the aggregated attendance statistics for event categories.

----------

## Improvements to be done
- **[EXISTING] Reporting Services**: Implement a dedicated service for exporting attendance data into standard formats (PDF, CSV, Excel) for offline processing.
- **[EXISTING] Frontend Sync**: Use the `attendanceDuration` and `attendanceTimeStart` from the `EventCategory` to drive a real-time countdown/window on the user dashboard.
- **[EXISTING] Historical Analysis**: Enhance the `GetEventCategoryStatsQuery` to allow for date-range filtering and comparative analysis between different periods.
- **[NEW] Timezone Support**: Add timezone-aware attendance handling — currently `CreateAttendanceCommand` uses `LocalTime.now()` which relies on the server JVM timezone, making it inconsistent for clients in different zones.
- **[NEW] Input Validation**: Add `@Valid` annotation on `AttendanceCreateDTO` in the PUT endpoint to enforce proper request validation.
- **[NEW] Concurrent Marking Safety**: Although the DB has a unique constraint on `(user_id, event_id)`, `CreateAttendanceCommand` uses a check-then-save pattern. Under high concurrency two requests could pass the `existsByUserIdAndEventId` check and collide, resulting in a `DataIntegrityViolationException`. Use optimistic locking or a synchronized guard.
- **[NEW] Unused Dependency Cleanup**: `CreateAttendanceCommand` injects `UserRepository` but never uses it — remove it.

## Mistakes that have to be solved
- **[EXISTING] Statistical Fragility**: The current weighted average projection in `GetEventCategoryStatsQuery` relies on sequential list indexes. If an event is missed or deleted, the timeline calculation becomes skewed.
- **[NEW] Stats Query Misalignment**: `GetEventCategoryStatsQuery` fetches `counts` and `summaries` via two separate queries. Both are ordered by `a.event.date ASC` but without an `event.id` tie-breaker. If multiple events share the same date, the two lists can misalign, pairing the wrong count with the wrong event summary.
- **[NEW] Integer Division Truncation**: In `GetEventCategoryStatsQuery`, `average`, `projected`, and `attendanceRate` are computed with integer division, causing precision loss. For example, `9 / 10 = 0` instead of `0.9`.
- **[NEW] Overly Broad Stats Access**: `getEventCategoryStats` is protected only by `@PreAuthorize("isAuthenticated()")`, meaning any authenticated user can view attendance statistics for any category without any role or ownership check.
- **[NEW] Typo in Response Message**: `CreateAttendanceCommand` returns `"Succesfully joined attendance"` — should be `"Successfully joined attendance"`.

## SOLVED
- **[EXISTING → SOLVED] Strict Window Enforcement**: `CreateAttendanceCommand` already verifies that the current server time falls within the valid attendance window defined by `attendanceTimeStart` and `attendanceDuration` before persisting the record (lines 40–46).
- **[NEW → SOLVED] Duplicate Protection at DB Level**: The `Attendance` entity already has `@UniqueConstraint(columnNames = {"user_id","event_id"})` on the `Prezenta` table, preventing duplicate attendances at the database level even if the application-level check is bypassed.
