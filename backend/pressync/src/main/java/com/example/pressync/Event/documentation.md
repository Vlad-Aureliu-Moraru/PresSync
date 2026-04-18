# Event Module

## How it works
This module handles individual events that are instances of event categories.
It manages the creation, retrieval, and updating of events. Using the CQRS pattern,
`CommandHandlers` handle event creation and modification, while `QueryHandlers` handle
searching and retrieving event details. `EventController` exposes these as REST endpoints,
and `EventRepository` interacts with the database.

## Why it is structured this way
The use of CQRS ensures that complex logic for generating recurring events or updating existing events
is separated from simple read operations. This makes the codebase cleaner and more testable.

## Connection to other components
- **EventCategory Module**: Every event belongs to an `EventCategory`. Events are essentially occurrences spawned based on the configuration of their category.
- **Attendance Module**: Events are the target of attendance records. Users are marked as attended or absent for specific events.
- **Common Module (Scheduling)**: The schedulers in the common module automatically generate these events based on configuration.

----------

## Improvements to be done
- **Event Customization**: Add support for event-specific metadata like icons, colors, or custom descriptions to allow for better visual differentiation in the UI calendars.
- **Resource Conflict Detection**: Implement a validation layer to prevent scheduling multiple events from the same category or in the same physical/virtual room at overlapping times.
- **Auto-Archiving**: Develop a background job to automatically move events older than a specific threshold (e.g., 1 year) to an archive table to maintain primary table performance.

## Mistakes that have to be solved
- **Batch Management Gap**: There is currently no API endpoint or underlying logic for batch deleting or archiving old events, requiring manual database intervention for cleanup.
- **State Transition Validation**: The `UpdateEventCommand` should implement stricter checks to ensure an event cannot be marked as "Active" if its base date has already passed.
