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
- **[NEW] Replace @Data with @Getter + @Setter**: `@Data` on the `EventCategoryConfig` entity generates `equals`/`hashCode` including the `@OneToMany categories` collection. This can cause infinite recursion, performance issues, and unintended lazy loading during collection operations.
- **[NEW] Add orphanRemoval = true**: The `@OneToMany(mappedBy = "categoryConfig", cascade = CascadeType.ALL)` mapping lacks `orphanRemoval = true`. When a category is removed from the `categories` list, it becomes orphaned rather than deleted.
- **[NEW] Orphan Config Cleanup Service**: When a config is unlinked from all categories (e.g., via update from `repeatable=true` to `false`), the config remains in the database. Add a cleanup mechanism to remove orphaned config rows.

## Mistakes that have to be solved
- **[NEW] Monthly Preview Logic Bug**: `previewOccurrences` for `MONTHLY` uses `plusMonths(1)` which preserves day-of-month, not day-of-week. A config like "the 2nd Monday of every month" cannot be represented correctly — if the base date's day-of-week doesn't match `repeatsOnSpecificDay` in a given month, that entire month is skipped.
- **[NEW] Performance in Preview Loop**: For `MONTHLY` recurrence with a `repeatsOnSpecificDay`, `previewOccurrences` may loop through many months (skipping most) to find matching dates, causing degraded performance with large `limit` values.

## SOLVED
- **Rule Previewer**: Implemented a preview utility that returns upcoming occurrences from a config for UI validation.
- **Coupling Over-Engineering (Interim)**: Enforced a 1:1 logical constraint in the service layer to prevent config reuse while the schema remains unchanged.
- **Incomplete Validation**: Added validation to ensure `baseDate` aligns with `repeatsOnSpecificDay` and required fields are present.
