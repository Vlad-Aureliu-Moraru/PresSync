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
- **Architectural Consolidation**: Merge the `EventCategoryConfig` logic into this module to reduce the complexity of cross-module dependencies and simplify the domain model.
- **Exception Rule Support**: Add capabilities to define "exceptions" in the repetition logic (e.g., skip specific dates like public holidays or university breaks).
- **Template Versioning**: Implement versioning for event category templates so that historical attendance records remain tied to the correct configuration even if the template is updated for future events.

## Mistakes that have to be solved
- **Missing Time Validation**: The create and update command handlers are missing a cross-field validation check to ensure that `startingTime` is always before `endTime`.
- **Config Sync Inconsistency**: Ensure that updates to the category configuration trigger immediate re-calculation or status updates of the cached "Today's Schedule" in the `Common` module.
