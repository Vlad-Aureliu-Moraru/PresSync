# PresSync API Documentation

This document provides a comprehensive overview of the REST API endpoints exposed by the PresSync backend.

---

## Authentication & Authorization

Most endpoints require a valid JWT token passed in the `Authorization` header as `Bearer <token>`.

**Role hierarchy:** `USER` < `MODERATOR` < `ADMIN`. Higher roles inherit the permissions of lower ones.

### Error Response Schema

All error responses follow the same format:
```json
{
  "status": 401,
  "message": "Invalid email or password",
  "timestamp": 1716000000000,
  "path": "uri=/auth/login"
}
```

**Common HTTP status codes:**
- `200 OK` — Success
- `400 Bad Request` — Validation failure
- `401 Unauthorized` — Bad credentials, expired/invalid JWT, or account not activated
- `403 Forbidden` — Insufficient role
- `404 Not Found` — Resource does not exist
- `409 Conflict` — Data integrity violation (e.g., duplicate email)
- `423 Locked` — Account temporarily locked

---

### Authentication Module
**Base URL:** `/auth`
**Roles allowed:** None (public endpoints)

**`AuthenticationResponse` schema (applies to all auth endpoints):**
```json
{
  "token": "string | null",
  "requiresMfa": true | false,
  "otpDestination": "string | null"
}
```
- `token` — JWT when MFA is complete or not required
- `requiresMfa` — whether MFA OTP verification is needed
- `otpDestination` — masked email where OTP was sent (e.g. `v***l@...`)

#### Register User
- **Endpoint:** `POST /auth/register`
- **Purpose:** Registers a new user. MFA is **always** required after registration.
- **Body (JSON):** `UserCreateDTO`
    - `name` (String): The user's first name. [Required, 1-30 chars]
    - `surname` (String): The user's last name. [Required, 1-30 chars]
    - `email` (String): The user's email address (used for login). [Required]
    - `password` (String): The user's password. [Required, min 6 chars]
- **Responses:**
    - `200 OK`: Returns `AuthenticationResponse` with `requiresMfa=true` and `otpDestination`. `token` is `null`. Client must call `/auth/verify-otp` to complete authentication.
    - `400 Bad Request`: If validation fails.

#### Login
- **Endpoint:** `POST /auth/login`
- **Purpose:** Authenticates an existing user. MFA is enforced for ADMIN and MODERATOR roles; for regular USER, MFA is required only if the user has MFA enabled.
- **Body (JSON):** `AuthenticationRequest`
    - `email` (String): Registered email address. [Required]
    - `password` (String): User's password. [Required]
- **Responses:**
    - `200 OK` (no MFA): Returns `AuthenticationResponse` with `requiresMfa=false` and a JWT `token`.
    - `200 OK` (MFA required): Returns `AuthenticationResponse` with `requiresMfa=true` and `otpDestination`. `token` is `null`. Client must call `/auth/verify-otp`.
    - `401 Unauthorized`: If credentials are invalid or account not activated.

#### Verify OTP (MFA)
- **Endpoint:** `POST /auth/verify-otp`
- **Purpose:** Completes MFA verification by submitting the OTP code sent via email. Returns the JWT token on success.
- **Body (JSON):** `VerifyMfaRequest`
    - `email` (String): The email used during register/login. [Required]
    - `otpCode` (String): The 6-digit OTP code sent via email. [Required]
- **Responses:**
    - `200 OK`: Returns `AuthenticationResponse` with `requiresMfa=false` and the JWT `token`.
    - `401 Unauthorized`: If OTP is invalid or expired.

---

## User Management
**Base URL:** `/user`

### Shared Response DTOs

**`UserGetDTO`:**
```json2
{
  "id": 1,
  "name": "John",
  "surname": "Doe",
  "email": "john@example.com",
  "role": "USER",
  "active": true
}
```

#### Get All Users
- **Endpoint:** `GET /user`
- **Roles:** `ADMIN`, `MODERATOR`
- **Responses:**
    - `200 OK`: Returns a list of `UserGetDTO`.

#### Get User by ID
- **Endpoint:** `GET /user/{id}`
- **Roles:** `ADMIN`, `MODERATOR`, or the owning user
- **Parameters:** `id` (Path, Integer) — The database ID of the user. [Required]
- **Responses:**
    - `200 OK`: Returns the `UserGetDTO`.
    - `404 Not Found`: If the user does not exist.

#### Update User
- **Endpoint:** `PUT /user/{id}`
- **Roles:** `ADMIN` (any user) or the owning user (self only)
- **Parameters:**
    - `id` (Path, int) — The ID of the user to update. [Required]
    - **Body (JSON):** `UserUpdateRequestDTO`
        - `name` (String): Updated first name. [Required, 1-30 chars]
        - `surname` (String): Updated last name. [Required, 1-30 chars]
        - `email` (String): Updated email. [Required]
        - `password` (String): New password (null or blank to keep current). [Optional, min 6 chars]
        - `role` (String): `USER`, `MODERATOR`, or `ADMIN`. Only `ADMIN` users can change roles. [Optional]
- **Responses:**
    - `200 OK`: User updated successfully.

#### Delete User
- **Endpoint:** `DELETE /user/{id}`
- **Roles:** `ADMIN` only
- **Parameters:** `id` (Path, Integer) — The ID of the user to delete. [Required]
- **Responses:**
    - `200 OK`: User deleted successfully.

---

## Attendance Tracking
**Base URL:** `/attendance`

### Shared Response DTOs

**`AttendanceGetDTO`:**
```json
{
  "id": 1,
  "user": { "id": 1, "name": "John", "surname": "Doe", "email": "john@example.com", "role": "USER", "active": true },
  "event": { "id": 1, "eventCategory": { ... }, "active": true, "archived": false, "date": "2026-05-19" },
  "date": "2026-05-19T10:00:00"
}
```

**`Attendance` (raw entity):**
```json
{
  "id": 1,
  "user": { "id": 1, "name": "John", ... },
  "event": { "id": 1, ... },
  "joinedAt": "2026-05-19T10:00:00"
}
```

**`EventCategoryStatsDTO`:**
```json
{
  "averageAttendance": 15,
  "maxAttendance": 22,
  "minAttendance": 8,
  "projectedNextAttendance": 17
}
```

#### Get Category Statistics
- **Endpoint:** `GET /attendance/stats/category/{categoryId}`
- **Roles:** `ADMIN`, `MODERATOR`
- **Parameters:** `categoryId` (Path, int) — ID of the event category. [Required]
- **Responses:**
    - `200 OK`: Returns `EventCategoryStatsDTO`.

#### Get All Attendance Records
- **Endpoint:** `GET /attendance`
- **Roles:** `ADMIN`, `MODERATOR`
- **Responses:**
    - `200 OK`: List of `AttendanceGetDTO`.

#### Get Attendance by ID
- **Endpoint:** `GET /attendance/{id}`
- **Roles:** `ADMIN`, `MODERATOR`
- **Parameters:** `id` (Path, int) — The ID of the attendance record. [Required]
- **Responses:**
    - `200 OK`: Returns the raw `Attendance` entity.
    - `404 Not Found`: If the record does not exist.

#### Get Attendance by User
- **Endpoint:** `GET /attendance/user/{userId}`
- **Roles:** `ADMIN`, `MODERATOR`, or the owning user
- **Parameters:** `userId` (Path, int) — The ID of the user. [Required]
- **Responses:**
    - `200 OK`: List of raw `Attendance` entities.

#### Mark Attendance
- **Endpoint:** `POST /attendance/mark`
- **Roles:** `USER`, `MODERATOR`, `ADMIN`
- **Details:** Uses the authenticated user's ID from the JWT. Checks the attendance time window of the currently active event category.
- **Responses:**
    - `200 OK`: Attendance marked successfully.
    - `400 Bad Request`: If no active event, outside attendance window, or duplicate attendance.

#### Update Attendance
- **Endpoint:** `PUT /attendance/{id}`
- **Roles:** `ADMIN`, `MODERATOR`
- **Parameters:**
    - `id` (Path, int) — The ID of the attendance record. [Required]
    - **Body (JSON):** `AttendanceCreateDTO`
        - `userId` (int): New user ID.
        - `eventId` (int): New event ID.
- **Responses:**
    - `200 OK`: Record updated.
    - `404 Not Found`: If the record, user, or event does not exist.

---

## Event Management
**Base URL:** `/event`

### Shared Response DTOs

**`EventGetDTO`:**
```json
{
  "id": 1,
  "eventCategory": {
    "id": 1,
    "name": "Morning Lecture",
    "startingTime": "09:00:00",
    "endTime": "10:00:00",
    "attendanceTimeStart": "08:55:00",
    "attendanceDuration": 10,
    "repeatable": true,
    "specificDate": null,
    "categoryConfig": null
  },
  "active": true,
  "archived": false,
  "date": "2026-05-19"
}
```

#### Get All Events
- **Endpoint:** `GET /event`
- **Roles:** `USER`, `MODERATOR`, `ADMIN`
- **Responses:**
    - `200 OK`: List of `EventGetDTO`.

#### Get Event by ID
- **Endpoint:** `GET /event/{id}`
- **Roles:** `USER`, `MODERATOR`, `ADMIN`
- **Parameters:** `id` (Path, Integer) — The event ID. [Required]
- **Responses:**
    - `200 OK`: Returns `EventGetDTO`.
    - `404 Not Found`: If the event does not exist.

#### Create Event
- **Endpoint:** `POST /event`
- **Roles:** `ADMIN`, `MODERATOR`
- **Body (JSON):** Raw `Event` object
    - `eventCategory` (Object): The parent event category. [Required]
    - `active` (Boolean): Whether the event is active. [Optional]
    - `archived` (Boolean): Whether the event is archived. [Optional]
    - `date` (String, LocalDate): The date of the event. [Optional]
- **Responses:**
    - `200 OK`: Event created.
    - `400 Bad Request`: If invalid data is provided.

#### Update Event
- **Endpoint:** `PUT /event/{id}`
- **Roles:** `ADMIN`, `MODERATOR`
- **Parameters:**
    - `id` (Path, int) — The event ID. [Required]
    - **Body (JSON):** Raw `Event` object (same fields as Create).
- **Responses:**
    - `200 OK`: Event updated.
    - `404 Not Found`: If the event does not exist.

#### Delete Event
- **Endpoint:** `DELETE /event/{id}`
- **Roles:** `ADMIN`, `MODERATOR`
- **Parameters:** `id` (Path, Integer) — The event ID. [Required]
- **Responses:**
    - `200 OK`: Event deleted.
    - `404 Not Found`: If the event does not exist.

---

## Event Categories
**Base URL:** `/eventCategory`

### `EventCategory` fields returned by endpoints:
```json
{
  "id": 1,
  "name": "Morning Lecture",
  "startingTime": "09:00:00",
  "endTime": "10:00:00",
  "attendanceTimeStart": "08:55:00",
  "attendanceDuration": 10,
  "repeatable": true,
  "specificDate": null,
  "categoryConfig": { ... },
  "events": null
}
```

### Enum values

**`repeatableType`:**
- `DAILY`, `WEEKLY`, `BIWEEKLY`, `MONTHLY`, `YEARLY`

**`repeatsOnSpecificDay`:**
- `MON`, `TUE`, `WED`, `THU`, `FRI`, `SAT`, `SUN`, `NO`

#### List All Categories
- **Endpoint:** `GET /eventCategory`
- **Roles:** `USER`, `MODERATOR`, `ADMIN`
- **Responses:**
    - `200 OK`: List of `EventCategory` objects.

#### Get Category by ID
- **Endpoint:** `GET /eventCategory/{id}`
- **Roles:** `USER`, `MODERATOR`, `ADMIN`
- **Parameters:** `id` (Path, int) — The category ID. [Required]
- **Responses:**
    - `200 OK`: Returns `EventCategory`.
    - `404 Not Found`: If the category does not exist.

#### Get Categories Due Today
- **Endpoint:** `GET /eventCategory/today`
- **Roles:** `USER`, `MODERATOR`, `ADMIN`
- **Purpose:** Returns categories from the in-memory cache that have events scheduled for today (populated at midnight and on category changes).
- **Responses:**
    - `200 OK`: List of `EventCategory` objects.

#### Create Category
- **Endpoint:** `POST /eventCategory/create`
- **Roles:** `ADMIN` only
- **Body (JSON):** `CreateEventCategoryRequest`
    - `name` (String): Category name. [Required]
    - `startingTime` (Time, HH:MM:SS): Event start time. [Required]
    - `endTime` (Time, HH:MM:SS): Event end time. [Required, must be after startingTime]
    - `attendanceTimeStart` (Time, HH:MM:SS): When marking attendance becomes available. [Required, must be between startingTime and endTime]
    - `attendanceDuration` (Integer): Duration in minutes for the attendance window. [Required, min 5]
    - `repeatable` (Boolean): Whether the event repeats. [Required]
    - `configId` (Integer): ID of an existing `EventCategoryConfig` to reuse. [Optional, only if repeatable=true]
    - `repeatableType` (String): `DAILY`, `WEEKLY`, `BIWEEKLY`, `MONTHLY`, or `YEARLY`. [Required if repeatable=true and configId is null]
    - `repeatsOnSpecificDay` (String): `MON`-`SUN` or `NO`. The day of the week the event repeats on. [Required if repeatable=true and configId is null]
    - `baseDate` (String, LocalDate): The initial date for the schedule. Defaults to today. [Optional]
- **Responses:**
    - `200 OK`: Category created.
    - `400 Bad Request`: If validation fails or category collides with an existing one.

#### Update Category
- **Endpoint:** `PUT /eventCategory/{id}`
- **Roles:** `ADMIN` only
- **Parameters:**
    - `id` (Path, int): The category ID. [Required]
    - **Body (JSON):** `UpdateEventCategoryRequest` (same fields as Create).
- **Responses:**
    - `200 OK`: Category updated.
    - `400 Bad Request`: If validation fails or collision detected.
    - `404 Not Found`: If the category does not exist.

#### Delete Category
- **Endpoint:** `DELETE /eventCategory/{id}`
- **Roles:** `ADMIN` only
- **Parameters:** `id` (Path, int): The category ID. [Required]
- **Responses:**
    - `200 OK`: Category deleted.
    - `404 Not Found`: If the category does not exist.
