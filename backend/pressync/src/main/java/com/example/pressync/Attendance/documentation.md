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
- **Reporting Services**: Implement a dedicated service for exporting attendance data into standard formats (PDF, CSV, Excel) for offline processing.
- **Frontend Sync**: Use the `attendanceDuration` and `attendanceTimeStart` from the `EventCategory` to drive a real-time countdown/window on the user dashboard.
- **Historical Analysis**: Enhance the `GetEventCategoryStatsQuery` to allow for date-range filtering and comparative analysis between different periods.

## Mistakes that have to be solved
- **Statistical Fragility**: The current weighted average projection in `GetEventCategoryStatsQuery` relies on sequential list indexes. If an event is missed or deleted, the timeline calculation becomes skewed.
- **Strict Window Enforcement**: The `CreateAttendanceCommand` should explicitly verify if the current server time falls within the valid attendance window defined by the event's category before persisting the record.
