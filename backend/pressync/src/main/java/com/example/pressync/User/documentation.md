# User Module

## How it works
This module manages user accounts, authentication, and authorization within the application. It includes standard user CRUD operations (via CQRS Handlers) and authentication endpoints for logging in and registering via `AuthenticationController` and `UserController`. It also contains validation logic for user credentials and details.

## Why it is structured this way
Separating user management and authentication into its own module provides a centralized location for security principles. Using CQRS for user creation and retrieval prevents the security logic from becoming tangled with general application logic.

## Connection to other components
- **Config Module**: Integrates strongly with `SecurityConfig` to provide bounded access and session management (like generating/validating JWTs).
- **Attendance Module**: Forms the basis of an attendance record—an attendance entry links a specific user to an event.
- SERVICES (Auth): Utilizes the authentication service layer to verify credentials securely.

----------

## Improvements that were done
- **Granular RBAC**: Implement field-level and path-level Role-Based Access Control as specified in the `API_ROLES_REQUIREMENTS.md`.
- **Account Security**: Add a mechanism for account lockout after several failed login attempts to prevent brute-force attacks.
- **Audit Logs**: Implement a user activity logging system to track sensitive changes (e.g., password changes or role modifications).
- **MFA Support**: Add Multi-Factor Authentication (MFA) to increase security for administrative accounts.

## Mistakes that have to be solved
- **Role Update Lock**: The `UpdateUserCommand` currently hardcodes the previous role during updates (line 38), which prevents admins from changing a user's role through the standard update flow.
- **Validation Consistency**: Ensure that email uniqueness is checked globally before attempting a write operation to prevent DB constraint violations that aren't user-friendly.
