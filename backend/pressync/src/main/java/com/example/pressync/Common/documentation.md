# Common Module

## How it works
This module contains shared utilities and background tasks, primarily handling automated scheduling logic.
It features schedulers like `DailyLoaderScheduler` and `MinuteEventScheduler`,
which run periodically to check if new individual events need to be instantiated from repeatable event categories.
It also utilizes a caching component (`TodayScheduleCache`) for optimizing frequent data retrieval.

## Why it is structured this way
Housing automated, global background tasks in a `Common` module prevents domain models (like Event or User)
from being polluted with infrastructure scheduling code.
The caching is placed here because it's a cross-cutting concern used by these schedulers to avoid overloading
the database every minute.

## Connection to other components
- **EventCategoryConfig Module**: Reads from these configurations to understand when to fire event generation logic.
- **Event Module**: The primary consumer, as the schedulers generate and save new `Event` instances based on the rules interpreted.

----------

## Improvements to be done
- **[EXISTING] Standardized Logging**: Replace all `System.out.println` calls with a proper logging framework (SLF4J/Logback) to ensure logs are manageable and searchable in production.
- **[EXISTING] Configurable Scheduling**: Externalize the cron expressions and scheduler delay intervals into `application.properties` to allow deployment-specific tuning.
- **[EXISTING] Observability**: Add custom health indicators for the schedulers to monitor if the background jobs are failing or stalled.
- **[NEW] Clean Up Cron Expression**: `@Scheduled(cron = "0 0 0  * * *")` has extra spaces. Use `"0 0 0 * * *"` for consistency and to avoid parser ambiguity.
- **[NEW] Startup Reconciliation Job**: If the application is down during a scheduled event activation/deactivation time, those state transitions are lost. Add a startup reconciliation check to activate or archive events that were missed.
- **[NEW] Scheduler Shutdown Timeout**: `SchedulerConfig` sets `setWaitForTasksToCompleteOnShutdown(true)` but does not set `setAwaitTerminationSeconds(...)`, meaning an unresponsive scheduled task will hang shutdown indefinitely.
- **[NEW] Cache DTO Immutability**: `TodayScheduleCache` returns an unmodifiable list of `EventCategory` entities, but the entities themselves remain mutable. Consider returning immutable DTOs or defensive copies to prevent accidental mutation by consumers.

## Mistakes that have to be solved
- **[NEW] No Retry on Transient Failure**: `MinuteEventScheduler` catches `Exception` inside the loop and continues. If an event fails to start due to a transient database error (e.g., deadlock), the precise start time has already passed by the next tick — the event will never start.
- **[EXISTING] Race Condition in Cache**: The `DailyLoaderScheduler` clears the `todayList` before refilling it (line 41). If a separate thread (e.g., a controller request) accesses the cache during this window, it may receive incomplete data.

## SOLVED
- **[EXISTING → SOLVED] Fixed-Delay Fragility**: The `MinuteEventScheduler` uses `@Scheduled(cron = "0 * * * * *")` — cron-based execution — rather than a fixed delay. This ensures precise timing alignment rather than accumulating drift between ticks.
- **[NEW → SOLVED] Thread Safety in TodayScheduleCache**: `TodayScheduleCache` uses a `volatile` field and creates an unmodifiable copy of the list in `updateList()`, providing safe publication across threads. The race condition window is limited to the instant between clearing and refilling the list reference.
