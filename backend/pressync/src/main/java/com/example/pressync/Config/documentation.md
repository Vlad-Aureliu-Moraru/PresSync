# Config Module

## How it works
This module houses global application configurations, particularly focusing on Spring Security settings
(`SecurityConfig`). It defines HTTP security rules, CORS configurations, password encoders, and authentication providers
for the application context.

## Why it is structured this way
Keeping global Spring Boot configurations isolated in a `Config`
package is a standard best practice. It ensures that infrastructure
and security wiring does not bleed into business logic, making it easier
to audit and modify global settings.

## Connection to other components
- **User Module**: Works hand-in-hand with the user module to define which endpoints are public (like login/register) and which require an authenticated user context.
- **Services (Auth)**: Intertwined with filtering logic, deciding how incoming requests are authenticated using tokens handled by the auth services.

----------

## Improvements to be done
- **[LOW] Externalized Configuration**: Move all hardcoded variables into `application.properties` or environment variables for better environment parity. (Currently done for CORS origins and JWT secret key; still hardcoded: JWT expiration in `JWTService`, allowed CORS headers list.)
- **[LOW] Enhanced Security Monitoring**: Implement a `SecurityAuditLog` component to track all configuration changes and authentication failures centrally.
- **[LOW] Performance Tuning**: Optimize the `CorsConfigurationSource` to use more specific path-based configurations rather than a catch-all registry.
- **[MEDIUM] Add Security Headers**: Configure `X-Content-Type-Options`, `X-Frame-Options`, and a `Content-Security-Policy` header to harden the application against common web attacks.
- **[LOW] Expand Allowed CORS Headers**: Currently only `Authorization` and `Content-Type` are allowed. Add common headers like `X-Requested-With` to prevent frontend frameworks from triggering CORS errors.
- **[MEDIUM] Add Specific Exception Handlers**: The `GlobalExceptionHandler` is missing handlers for `MethodArgumentNotValidException`, `HttpMessageNotReadableException`, and `MissingServletRequestParameterException`. Validation errors fall through to the generic `Exception` handler.

## Mistakes that have to be solved
- **[MEDIUM] Exception Message Leak**: `GlobalExceptionHandler.handleGlobalException` returns `"An unexpected error occurred: " + ex.getMessage()`, which can leak internal implementation details (SQL errors, stack fragments, server paths) to API clients in production.
- **[LOW] CSRF Vulnerability**: CSRF protection is currently disabled via `.csrf(AbstractHttpConfigurer::disable)` (line 43). While acceptable for a stateless REST API, an analysis should be performed to ensure no session-based vectors are present.

## SOLVED
- **[SOLVED] Hardcoded CORS Policy**: CORS origins were previously hardcoded but are now externalized via `@Value("${app.cors.allowed-origins:http://localhost:4200,http://localhost:5173}")`, making deployments across environments straightforward.
