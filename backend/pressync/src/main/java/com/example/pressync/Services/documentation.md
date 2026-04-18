# Services Module

## How it works
This module contains cross-cutting domain services that aren't strict data access or application-level CQRS handlers. Specifically, it holds authentication infrastructure (`Auth`)—such as JWT generation, parsing, and validation—and custom exception handling logic (`Exceptions`) for centralized error formatting.

## Why it is structured this way
By isolating these common services, other modules (like User and Config) can depend on a shared library of logic. This prevents code duplication (like JWT parsing logic) across different controllers or handlers. Exception formatting is centralized to maintain a consistent API response structure for clients.

## Connection to other components
- **User Module**: Employed heavily by the `AuthenticationController` to issue and validate tokens upon login/registration.
- **Config Module**: The security configurations use components from this Auth service layer to intercept and authenticate requests dynamically.
- System-wide: The defined exceptions are thrown consistently across the entire application's handlers.

----------

## Improvements to be done
- **Unit Test Coverage**: Create a dedicated test suite for `JWTService` using various edge cases (expired tokens, malformed strings, and incorrect keys) to ensure robust security validation.
- **Communication Service Layer**: Implement a generic notification service (supporting Email/SMS) that can be easily injected into any module for alerts.
- **Async Processing**: Offload non-blocking service tasks (like sending emails or logging audit events) to background threads using Spring's `@Async` capabilities.

## Mistakes that have to be solved
- **Service Responsibility Leak**: Some logic in the `Auth` services could be refactored into domain-specific handlers if the authentication process becomes more complex, maintaining the strictness of the CQRS structure.
- **Exception Over-Simplification**: The current exception handlers may leak stack traces to the client in certain scenarios; ensure all generic exceptions are caught and sanitized.
