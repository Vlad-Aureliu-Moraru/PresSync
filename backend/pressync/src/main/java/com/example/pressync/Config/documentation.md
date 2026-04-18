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
- **Externalized Configuration**: Move all hardcoded variables (CORS origins, JWT secrets, database parameters) into `application.properties` or environment variables for better environment parity.
- **Enhanced Security Monitoring**: Implement a `SecurityAuditLog` component to track all configuration changes and authentication failures centrally.
- **Performance Tuning**: Optimize the `CorsConfigurationSource` to use more specific path-based configurations rather than a catch-all registry.

## Mistakes that have to be solved
- **Hardcoded CORS Policy**: The `SecurityConfig` currently has hardcoded allowed origins (line 73), which makes deploying the application across different environments (Staging vs Production) difficult.
- **CSRF Vulnerability**: CSRF protection is currently disabled via `.csrf(AbstractHttpConfigurer::disable)` (line 43). While acceptable for a stateless REST API, an analysis should be performed to ensure no session-based vectors are present.
