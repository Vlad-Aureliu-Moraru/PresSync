# User Module

## How it works
This module manages user accounts, authentication, and authorization within the application. It includes standard user CRUD operations (via CQRS Handlers) and authentication endpoints for logging in and registering via `AuthenticationController` and `UserController`. It also contains validation logic for user credentials and details.

## Why it is structured this way
Separating user management and authentication into its own module provides a centralized location for security principles. Using CQRS for user creation and retrieval prevents the security logic from becoming tangled with general application logic.

## Connection to other components
- **Config Module**: Integrates strongly with `SecurityConfig` to provide bounded access and session management (like generating/validating JWTs).
- **Attendance Module**: Forms the basis of an attendance record—an attendance entry links a specific user to an event.
- **SERVICES (Auth)**: Utilizes the authentication service layer to verify credentials securely.

----------

## Improvements to be done
- **[MEDIUM] Granular RBAC**: Implement field-level and path-level Role-Based Access Control as specified in the `API_ROLES_REQUIREMENTS.md`.
- **[MEDIUM] Account Security**: Add a mechanism for account lockout after several failed login attempts to prevent brute-force attacks.
- **[LOW] Audit Logs**: Implement a user activity logging system to track sensitive changes (e.g., password changes or role modifications).
- **[MEDIUM] MFA Support**: Add Multi-Factor Authentication (MFA) to increase security for administrative accounts.
- **[CRITICAL] Fix Data Loss in UpdateUserCommand**: `UpdateUserCommand` creates a **new** `User()` instance and copies fields manually instead of mutating the fetched `foundUser`. Any field not explicitly copied (e.g., `createdAt`, `updatedAt`, or future additions) will be lost or overwritten with defaults.
- **[LOW] Add @Valid on Request Bodies**: Add `@Valid` annotation on `UserUpdateRequestDTO` and `AuthenticationRequest` in controllers to enforce validation constraints.
- **[MEDIUM] Implement Soft Delete**: Replace hard delete with soft delete (setting `active=false` or a `deletedAt` timestamp) to prevent foreign key constraint violations when users have related `Attendance` records.
- **[LOW] Self-Deletion Prevention**: Add a check to prevent an admin from deleting their own account.
- **[LOW] Password Strength Validation**: Add complexity requirements beyond minimum length (e.g., uppercase, digit, special character).
- **[MEDIUM] Rate Limiting on Auth Endpoints**: Add rate limiting on `/auth/login`, `/auth/register`, and `/auth/verify-otp` to mitigate brute force and enumeration attacks.

## Mistakes that have to be solved
- **[LOW] UpdateUserCommand Uses orElse(null)**: `userRepository.findById(id).orElse(null)` followed by a manual null check should be `orElseThrow()`.
- **[CRITICAL] Hard Delete Without FK Check**: `DeleteUserCommand` performs a hard delete without checking for related `Attendance` records, which will cause a foreign key constraint violation.

## SOLVED
- **[SOLVED] Role Update Lock**: The `resolveUpdatedRole` method in `UpdateUserCommand` correctly checks if the authenticated user has the `ADMIN` role and allows role changes for admins. The previous issue where the previous role was hardcoded has been resolved.
- **[SOLVED] Validation Consistency**: `UserValidator` now checks email uniqueness globally in both overloads — the `validate(name, surname, email, userId)` version allows the same user to keep their email, while `validate(name, surname, email)` (used for registration) throws if the email is already in use by any account. This prevents unhelpful DB constraint violations.
