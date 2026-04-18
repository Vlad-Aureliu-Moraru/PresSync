# API Roles & Requirements

This document maps the PresSync backend endpoints to their authorized user roles and permission levels. This serves as the blueprint for Role-Based Access Control (RBAC) implementation.

---

## 1. System Roles Definition

The system recognizes three primary roles, each with increasing levels of authority:

| Role | Description | Core Capabilities |
| :--- | :--- | :--- |
| **USER** | Standard registered attendee. | View events, mark own attendance. |
| **MODERATOR**| Event and schedule manager. | Manage events, update attendance records, view statistics. |
| **ADMIN** | System administrator. | Full system access, user management, category configuration. |

---

## 2. Global Access Rules

- **Public Access**: Authentication (`/auth/**`) is open to all users.
- **Default Requirement**: All non-auth endpoints require a valid JWT token.
- **Resource Ownership**: Users generally have "Read" and "Update" permissions on their own profile/attendance records regardless of their primary role.

---

## 3. Endpoint Authorization Mapping

### Authentication Module (`/auth`)
| Endpoint | Method | Authorized Roles | Permission Level |
| :--- | :--- | :--- | :--- |
| `/register` | `POST` | Public | Create Account |
| `/login` | `POST` | Public | Authenticate |

### User Management (`/user`)
| Endpoint | Method | Authorized Roles | Permission Level |
| :--- | :--- | :--- | :--- |
| `/user` | `GET` | ADMIN, MODERATOR | Read (All) |
| `/user/{id}` | `GET` | ADMIN, MODERATOR, USER (Self) | Read |
| `/user/{id}` | `PUT` | ADMIN, USER (Self) | Update |
| `/user/{id}` | `DELETE` | ADMIN | Delete |

### Attendance Tracking (`/attendance`)
| Endpoint | Method | Authorized Roles | Permission Level |
| :--- | :--- | :--- | :--- |
| `/stats/category/{categoryId}` | `GET` | ADMIN, MODERATOR | Read Analytics |
| `/attendance` | `GET` | ADMIN, MODERATOR | Read (All) |
| `/attendance/{id}` | `GET` | ADMIN, MODERATOR | Read |
| `/attendance/user/{userId}` | `GET` | ADMIN, MODERATOR, USER (Self) | Read (Filtered) |
| `/mark` | `POST` | USER, MODERATOR, ADMIN | Create (Own Entry) |
| `/attendance/{id}` | `PUT` | ADMIN, MODERATOR | Update |

### Event Management (`/event`)
| Endpoint | Method | Authorized Roles | Permission Level |
| :--- | :--- | :--- | :--- |
| `/event` | `GET` | USER, MODERATOR, ADMIN | Read (All) |
| `/event/{id}` | `GET` | USER, MODERATOR, ADMIN | Read |
| `/event` | `POST` | ADMIN, MODERATOR | Create |
| `/event/{id}` | `PUT` | ADMIN, MODERATOR | Update |
| `/event/{id}` | `DELETE` | ADMIN, MODERATOR | Delete |

### Event Category Management (`/eventCategory`)
| Endpoint | Method | Authorized Roles | Permission Level |
| :--- | :--- | :--- | :--- |
| `/eventCategory` | `GET` | USER, MODERATOR, ADMIN | Read (All) |
| `/eventCategory/{id}` | `GET` | USER, MODERATOR, ADMIN | Read |
| `/create` | `POST` | ADMIN | Create Category |
| `/{id}` | `PUT` | ADMIN | Update Category |
| `/{id}` | `DELETE` | ADMIN | Delete Category |

---

## 4. Implementation Notes

> [!NOTE]
> Authorization is currently verified via JWT claims. In a future update, these requirements should be enforced using Spring Security's `@PreAuthorize` annotations on the controller methods or via `requestMatchers` in `SecurityConfig.java`.

> [!IMPORTANT]
> A "MODERATOR" can manage events for any category but cannot delete event categories or create new ones; these are reserved for "ADMIN" to maintain top-level organizational structure.
