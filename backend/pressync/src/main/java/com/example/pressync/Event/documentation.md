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
- **[LOW] Event Customization**: Add support for event-specific metadata like icons, colors, or custom descriptions to allow for better visual differentiation in the UI calendars.
- **[LOW] Resource Conflict Detection**: Implement a validation layer to prevent scheduling multiple events from the same category or in the same physical/virtual room at overlapping times.
- **[LOW] Auto-Archiving**: Develop a background job to automatically move events older than a specific threshold (e.g., 1 year) to an archive table to maintain primary table performance.
- **[MEDIUM] Use Managed Entity References**: In `CreateEventCommand`, the fetched `EventCategory` from `findById` is verified to exist but is never set on the event — the event is saved with the original (potentially detached) `EventCategory` object. Replace it with the managed reference.
- **[MEDIUM] Use DTOs Instead of Raw Entities**: The controller currently accepts raw `Event` entities in create/update endpoints. Use dedicated DTOs to prevent field injection attacks and decouple the API contract from the persistence model.
- **[LOW] Use orElseThrow Consistently**: `UpdateEventCommand` uses `orElse(null)` followed by an explicit null check. Replace with `orElseThrow(() -> new IllegalArgumentException(...))` for consistency.
- **[LOW] Add @Valid on Request Bodies**: Add `@Valid` annotations on `EventPutDTO` and any future DTOs in the controller.
- **[MEDIUM] Add @Transactional to UpdateEventCommand**: Currently `UpdateEventCommand` has no `@Transactional` annotation, unlike `CreateEventCommand`.
- **[LOW] Startup Reconciliation**: Add a job on application startup to check if any events should have been activated or deactivated while the server was down.

## Mistakes that have to be solved
- **[LOW] Return Type Mismatch**: `EventController.updateEvent` declares `ResponseEntity<Event>` as its return type but returns `ResponseEntity.ok().build()` (empty body).
- **[LOW] Raw ResponseEntity**: `EventController.deleteEvent` returns a raw `ResponseEntity` — should specify the generic type.
- **[MEDIUM] Detached Entity Risk**: `CreateEventCommand` verifies the category exists via `findById` but saves the event with the original `EventCategory` from the request body, which may be a detached entity with stale or incomplete data.
- **[MEDIUM] No EventCategory Validation in UpdateEventCommand**: When `incoming.getEventCategory() != null`, it sets the category directly without verifying it exists in the database.
- **[LOW] Batch Management Gap**: There is currently no API endpoint or underlying logic for batch deleting or archiving old events, requiring manual database intervention for cleanup.
- **[LOW] State Transition Validation**: The `UpdateEventCommand` should implement stricter checks to ensure an event cannot be marked as "Active" if its base date has already passed.

## SOLVED
_No items currently resolved._
