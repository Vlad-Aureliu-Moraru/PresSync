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
- **Standardized Logging**: Replace all `System.out.println` calls with a proper logging framework (SLF4J/Logback) to ensure logs are manageable and searchable in production.
- **Configurable Scheduling**: Externalize the cron expressions and scheduler delay intervals into `application.properties` to allow deployment-specific tuning.
- **Observability**: Add custom health indicators for the schedulers to monitor if the background jobs are failing or stalled.

## Mistakes that have to be solved
- **Race Condition in Cache**: The `DailyLoaderScheduler` clears the `todayList` before refilling it (line 41). If a separate thread (e.g., a controller request) accesses the cache during this window, it may receive incomplete data.
- **Fixed-Delay Fragility**: The `MinuteEventScheduler` (if implemented with fixed delay) might accumulate drift over time; it should use fixed-rate or cron-based execution for strict temporal accuracy.
