# EventCategoryConfig Module

## How it works
This module encapsulates the configuration and rules (like recurrence, start time, and time intervals) for `EventCategory` entities.
It manages data regarding when and how often an event category should trigger individual events.

## Why it is structured this way
Abstracting the configuration into its own module/entity simplifies the `EventCategory` model. It allows for complex configurations
(like cron expressions, repeatable rules, and active periods) to be decoupled, making it easier to manage, validate,
and update scheduling logic independently from the base category details.

## Connection to other components
- **EventCategory Module**: Directly linked to an `EventCategory`, acting as its rules engine.
- **Common Module (Schedulers)**: Serves as the source of truth for the daily and minute-based schedulers that automatically instantiate events.

----------

## Improvements to be done
_No pending improvements; current architecture is intentional (recommended by instructor to avoid overloading `EventCategory`)._

## Mistakes that have to be solved
_No known mistakes; current separation is intentional._

## SOLVED
- **Rule Previewer**: Implemented a preview utility that returns upcoming occurrences from a config for UI validation.
- **Coupling Over-Engineering (Interim)**: Enforced a 1:1 logical constraint in the service layer to prevent config reuse while the schema remains unchanged.
- **Incomplete Validation**: Added validation to ensure `baseDate` aligns with `repeatsOnSpecificDay` and required fields are present.
