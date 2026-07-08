# Services Module

## How it works
This module contains cross-cutting domain services that aren't strict data access or application-level CQRS handlers. Specifically, it holds authentication infrastructure (`Auth`)—such as JWT generation, parsing, and validation—and custom exception handling logic (`Exceptions`) for centralized error formatting.

## Why it is structured this way
By isolating these common services, other modules (like User and Config) can depend on a shared library of logic. This prevents code duplication (like JWT parsing logic) across different controllers or handlers. Exception formatting is centralized to maintain a consistent API response structure for clients.

## Connection to other components
- **User Module**: Employed heavily by the `AuthenticationController` to issue and validate tokens upon login/registration.
- **Config Module**: The security configurations use components from this Auth service layer to intercept and authenticate requests dynamically.
- **System-wide**: The defined exceptions are thrown consistently across the entire application's handlers.

----------

## Improvements to be done
- **[MEDIUM] Unit Test Coverage**: Create a dedicated test suite for `JWTService` using various edge cases (expired tokens, malformed strings, and incorrect keys) to ensure robust security validation.
- **[LOW] Communication Service Layer**: Implement a generic notification service (supporting Email/SMS) that can be easily injected into any module for alerts.
- **[LOW] Async Processing**: Offload non-blocking service tasks (like sending emails or logging audit events) to background threads using Spring's `@Async` capabilities.
- **[LOW] Fix Email Masking Bug**: `maskEmail("user@example.com")` currently returns `u****r@example.com` (incorrect) instead of `u****@example.com`. The substring index calculation uses `atIndex - 1` instead of `atIndex`.
- **[LOW] Reuse SecureRandom Instance**: `generateOtpCode` instantiates `new SecureRandom()` on every call. `SecureRandom` is expensive to initialize; use a static or instance field instead.
- **[LOW] Make JWT Expiration Configurable**: JWT expiration is hardcoded to `1000 * 60 * 60 * 24` (24 hours) in `JWTService`. Move to `application.properties` via `@Value`.
- **[MEDIUM] Add Token Revocation Mechanism**: JWTs are stateless — if a user is demoted, locked, or deleted, their existing token remains valid until expiration. Implement a token blacklist or short TTL with refresh tokens.
- **[MEDIUM] Add Rate Limiting on OTP**: The `/auth/verify-otp` endpoint has no rate limiting or maximum attempt counter, making it vulnerable to brute force.

## Mistakes that have to be solved
- **[CRITICAL] Stale JWT Privilege Escalation**: `JWTService.isTokenValid` only checks the username (email) and expiration. It does **not** verify that the user is still `active`, that their role has not changed, or that the account is not locked. A demoted or deactivated user's token remains valid until expiry.
- **[MEDIUM] User Enumeration via OTP**: `AuthenticationService.verifyOtp` throws `BadCredentialsException("User not found")` if the email does not exist, while other branches return generic messages. This allows an attacker to enumerate which emails are registered.
- **[MEDIUM] MFA Inconsistency**: `AuthenticationService.authenticate` enforces MFA/OTP for `ADMIN` and `MODERATOR` roles but bypasses it entirely for `USER` accounts beyond the initial registration. This means regular user accounts have no MFA protection after the first login.
- **[LOW] Service Responsibility Leak**: Some logic in the `Auth` services could be refactored into domain-specific handlers if the authentication process becomes more complex, maintaining the strictness of the CQRS structure.
- **[LOW] Exception Over-Simplification**: The current exception handlers may leak stack traces to the client in certain scenarios; ensure all generic exceptions are caught and sanitized.

## SOLVED
_No items currently resolved. See related items in `Config` and `Common` module docs for resolved cross-cutting concerns._
