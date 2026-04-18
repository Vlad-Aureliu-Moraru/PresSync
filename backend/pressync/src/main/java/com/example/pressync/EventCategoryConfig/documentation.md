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
- **Architectural Simplification**: Deprecate this independent module and fold the configuration entities directly into the `EventCategory` domain to simplify querying and reduce database join overhead.
- **Rule Previewer**: Add a utility (either in the service or as a query) to "preview" the next 5 occurrences based on a given configuration for UI validation.

## Mistakes that have to be solved
- **Coupling Over-Engineering**: The level of separation between `EventCategory` and `EventCategoryConfig` increases architectural complexity without providing significant benefits (since a category normally has exactly one configuration).
- **Incomplete Validation**: Specific rules for `BIWEEKLY` or `MONTHLY` repetitions should be more strictly validated against the `baseDate` to prevent impossible scheduling scenarios.
