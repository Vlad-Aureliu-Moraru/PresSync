# EventCategory Module

## How it works
This module manages the categories/templates for events (e.g., a "Math Class" or a "Weekly Meeting").
It handles creating and updating event categories using `CommandHandlers` and retrieving them via `QueryHandlers`.
The `EventCategoryController` provides the API interface, whilst the `EventCategoryRepository` stores the data.

## Why it is structured this way
By treating `EventCategory` as a distinct entity from `Event`,
the system can define templates and rules for recurring events without
needing to duplicate scheduling logic manually for every occurrence.

## Connection to other components
- **EventCategoryConfig Module**: Defines the complex recurring rules and configurations for how an event category should spawn individual events.
- **Event Module**: Acts as the template from which individual `Event` instances are instantiated.
- **Attendance Module**: Statistics query handlers within the Attendance module calculate metrics dynamically based on the event category.

----------

## Improvements to be done
- **[EXISTING] Architectural Consolidation**: Merge the `EventCategoryConfig` logic into this module to reduce the complexity of cross-module dependencies and simplify the domain model.
- **[EXISTING] Exception Rule Support**: Add capabilities to define "exceptions" in the repetition logic (e.g., skip specific dates like public holidays or university breaks).
- **[EXISTING] Template Versioning**: Implement versioning for event category templates so that historical attendance records remain tied to the correct configuration even if the template is updated for future events.
- **[NEW] Extract Shared Validation**: `CreateEventCategoryCommand` and `UpdateEventCategoryCommand` contain identical copies of `checkValidity`, `isColidingWithSomething`, and `doDaysOverlap` methods. Extract these into a shared `EventCategoryValidator` service to eliminate the DRY violation.
- **[NEW] Migrate to java.time API**: Replace `Calendar.getInstance()` and `java.sql.Time` usage in `checkValidity` with `java.time.LocalTime` and `java.time.Duration` for thread safety, clarity, and immutability.
- **[NEW] Database-Level Collision Detection**: Replace `eventCategoryRepository.findAll()` (full table scan) in `isCollidingWithSomething` with a time-range database query to avoid loading all categories into memory on every create/update.
- **[NEW] Add @Valid on Request Bodies**: Add `@Valid` annotations on `CreateEventCategoryRequest` and `UpdateEventCategoryRequest` in the controller to enforce validation.
- **[NEW] Orphan Config Cleanup**: When updating a category from `repeatable=true` to `repeatable=false`, the old `EventCategoryConfig` is unlinked but not deleted. Add cleanup logic to remove orphaned config rows when no other category references them.
- **[NEW] Soft Delete Support**: Implement soft delete for categories that have historical events or attendance records to prevent referential integrity violations.

## Mistakes that have to be solved
- **[NEW] Missing @Transactional on UpdateEventCategoryCommand**: If `checkValidity` or `isColidingWithSomething` throws after `configRepository.save(config)` has been called, the config entity is persisted but the category update is rolled back, leaving an orphan config record. The create command has `@Transactional` but update does not.
- **[NEW] Typo in Method Name**: `isColidingWithSomething` should be `isCollidingWithSomething`.
- **[NEW] Missing Null Checks**: `checkValidity` dereferences `startingTime`, `endTime`, and `attendanceTimeStart` without null checks. If any of these are null, a `NullPointerException` will be thrown instead of a meaningful validation error.
- **[NEW] Raw ResponseEntity in Controller**: `EventCategoryController.deleteEventCategory` returns a raw `ResponseEntity` (no generic type).

## SOLVED
- **[EXISTING → SOLVED] Missing Time Validation**: Both create and update command handlers already validate via `checkValidity()` that `startingTime` is before `endTime`, `attendanceTimeStart` falls between the two, and the attendance duration does not exceed the event window.
- **[EXISTING → SOLVED] Config Sync Inconsistency**: Both `CreateEventCategoryCommand` and `UpdateEventCategoryCommand` publish `EventCategoryChangedEvent`, which the `DailyLoaderScheduler` listens for via `@EventListener`, triggering an immediate `fillTodaySchedule()` call to refresh the cached "Today's Schedule".
