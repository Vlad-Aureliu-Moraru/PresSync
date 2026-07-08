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
- **[LOW] Reporting Services**: Implement a dedicated service for exporting attendance data into standard formats (PDF, CSV, Excel) for offline processing.
- **[LOW] Frontend Sync**: Use the `attendanceDuration` and `attendanceTimeStart` from the `EventCategory` to drive a real-time countdown/window on the user dashboard.
- **[LOW] Historical Analysis**: Enhance the `GetEventCategoryStatsQuery` to allow for date-range filtering and comparative analysis between different periods.
- **[MEDIUM] Concurrent Marking Safety**: Although the DB has a unique constraint on `(user_id, event_id)`, `CreateAttendanceCommand` uses a check-then-save pattern. Under high concurrency two requests could pass the `existsByUserIdAndEventId` check and collide, resulting in a `DataIntegrityViolationException`. Use optimistic locking or a synchronized guard.

## Mistakes that have to be solved
- **[MEDIUM] Statistical Fragility**: The current weighted average projection in `GetEventCategoryStatsQuery` still relies on sequential list indexes. If an event is missed or deleted, the timeline calculation becomes skewed.

## SOLVED
- **[SOLVED] Strict Window Enforcement**: `CreateAttendanceCommand` already verifies that the current server time falls within the valid attendance window defined by `attendanceTimeStart` and `attendanceDuration` before persisting the record.
- **[SOLVED] Duplicate Protection at DB Level**: The `Attendance` entity already has `@UniqueConstraint(columnNames = {"user_id","event_id"})` on the `Prezenta` table, preventing duplicate attendances at the database level even if the application-level check is bypassed.
- **[SOLVED] Stats Query Misalignment**: Added `a.event.id ASC` as a secondary sort to both `countAttendancePerEventByCategory` and `getEventAttendanceSummariesByCategory` queries to ensure deterministic ordering when multiple events share the same date.
- **[SOLVED] Integer Division Truncation**: `average`, `projected`, and `attendanceRate` now use `Math.round()` with proper `double` casts to prevent precision loss in `GetEventCategoryStatsQuery`.
- **[SOLVED] UpdateAttendanceCommand Data Loss**: Fixed to fetch the existing `Attendance` record via `findById()` instead of creating a new instance, preserving `joinedAt` and avoiding data loss on update.
- **[SOLVED] Input Validation**: Added `@Valid` annotation on the `AttendanceCreateDTO` parameter in `AttendanceController.updateAttendanceById` and added `@NotNull`/`@Min` validation annotations on the DTO fields.
- **[SOLVED] Raw ResponseEntity**: `AttendanceController.updateAttendanceById` now returns `ResponseEntity<String>` instead of raw `ResponseEntity`.
- **[SOLVED] Unused Dependency Removed**: Removed unused `UserRepository` injection from `CreateAttendanceCommand`.
- **[SOLVED] Typo in Response**: Fixed `"Succesfully"` → `"Successfully"` in `CreateAttendanceCommand`.
- **[SOLVED] JPA Entity Annotations**: Replaced `@Data` with `@Getter`/`@Setter` on `Attendance` entity to avoid `equals()`/`hashCode()` issues with `@ManyToOne` associations and lazy loading.
- **[SOLVED] Unused Imports Removed**: Removed unused `UserGetDTO` import from `Attendance.java` and `EventPutDTO` import from `AttendanceGetDTO.java`.
- **[SOLVED] orElse(null) → orElseThrow()**: Replaced `orElse(null)` followed by manual null check with `orElseThrow()` in `GetAttendanceByIdQuery`.
- **[SOLVED] Missing Category Validation**: Added `EventCategoryRepository` injection in `GetAttendanceByUserIdAndCategoryIdQuery` to validate that the requested category exists before querying.
