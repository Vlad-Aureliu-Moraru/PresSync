# PresSync API Documentation

This document provides a comprehensive overview of the REST API endpoints exposed by the PresSync backend.

---

## Authentication & Authorization
Most endpoints require a valid JWT token passed in the `Authorization` header as `Bearer <token>`.

### Authentication Module
**Base URL:** `/auth`

#### Register User
- **Endpoint:** `POST /register`
- **Method:** `POST`
- **Purpose:** Registers a new user and returns an authentication token.
- **Parameters:**
    - **Body (JSON):** `UserCreateDTO`
        - `name` (String): The user's first name. [Required]
        - `surname` (String): The user's last name. [Required]
        - `email` (String): The user's email address (used for login). [Required]
        - `password` (String): The user's password. [Required]
- **Responses:**
    - `200 OK`: Returns `AuthenticationResponse` containing the JWT `token`.
    - `400 Bad Request`: If invalid data is provided.

#### Login
- **Endpoint:** `POST /login`
- **Method:** `POST`
- **Purpose:** Authenticates an existing user.
- **Parameters:**
    - **Body (JSON):** `AuthenticationRequest`
        - `email` (String): Registered email address. [Required]
        - `password` (String): User's password. [Required]
- **Responses:**
    - `200 OK`: Returns `AuthenticationResponse` containing the JWT `token`.
    - `401 Unauthorized`: If credentials are invalid.

---

## User Management
**Base URL:** `/user`

#### Get All Users
- **Endpoint:** `GET /user`
- **Method:** `GET`
- **Purpose:** Retrieves a list of all registered users.
- **Responses:**
    - `200 OK`: Returns a list of `UserGetAllDTO`.

#### Get User by ID
- **Endpoint:** `GET /user/{id}`
- **Method:** `GET`
- **Purpose:** Retrieves details for a specific user.
- **Parameters:**
    - `id` (Path, Integer): The database ID of the user. [Required]
- **Responses:**
    - `200 OK`: Returns the `UserGetAllDTO`.
    - `404 Not Found`: If the user does not exist.

#### Update User
- **Endpoint:** `PUT /user/{id}`
- **Method:** `PUT`
- **Purpose:** Updates user details.
- **Parameters:**
    - `id` (Path, int): The ID of the user to update. [Required]
    - **Body (JSON):** `UserCreateDTO` (Updates name, surname, email, and password).
- **Responses:**
    - `200 OK`: User updated successfully.

#### Delete User
- **Endpoint:** `DELETE /user/{id}`
- **Method:** `DELETE`
- **Purpose:** Removes a user from the system.
- **Parameters:**
    - `id` (Path, Integer): The ID of the user to delete. [Required]
- **Responses:**
    - `200 OK`: User deleted successfully.

---

## Attendance Tracking
**Base URL:** `/attendance`

#### Get Category Statistics
- **Endpoint:** `GET /stats/category/{categoryId}`
- **Method:** `GET`
- **Purpose:** Retrieves aggregated attendance statistics (average, max, attendance rate) for a specific event category.
- **Parameters:**
    - `categoryId` (Path, int): ID of the event category. [Required]
- **Responses:**
    - `200 OK`: Returns `EventCategoryStatsDTO`.

#### Get All Attendance Records
- **Endpoint:** `GET /attendance`
- **Method:** `GET`
- **Purpose:** Fetches all attendance entries in the system.
- **Responses:**
    - `200 OK`: List of `Attendance` objects.

#### Get Attendance by ID
- **Endpoint:** `GET /attendance/{id}`
- **Method:** `GET`
- **Purpose:** Retrieves a single attendance record.
- **Parameters:**
    - `id` (Path, int): The ID of the attendance record. [Required]
- **Responses:**
    - `200 OK`: Returns the `Attendance` object.

#### Get Attendance by User
- **Endpoint:** `GET /user/{userId}`
- **Method:** `GET`
- **Purpose:** Retrieves all attendance records associated with a specific user.
- **Parameters:**
    - `userId` (Path, int): The ID of the user. [Required]
- **Responses:**
    - `200 OK`: List of `Attendance` objects.

#### Mark Attendance
- **Endpoint:** `POST /mark`
- **Method:** `POST`
- **Purpose:** Marks the currently authenticated user as present for the active event.
- **Additional Details:** Implicitly uses the authenticated user's ID from the JWT.
- **Responses:**
    - `200 OK`: Attendance marked successfully.

#### Update Attendance
- **Endpoint:** `PUT /attendance/{id}`
- **Method:** `PUT`
- **Purpose:** Manually updates an existing attendance record.
- **Parameters:**
    - `id` (Path, int): The ID of the attendance record. [Required]
    - **Body (JSON):** `AttendanceCreateDTO`
        - `userId` (int): New user ID.
        - `eventId` (int): New event ID.
- **Responses:**
    - `200 OK`: Record updated.

---

## Event Management
**Base URL:** `/event`

#### Get All Events
- **Endpoint:** `GET /event`
- **Method:** `GET`
- **Purpose:** Lists all scheduled events.
- **Responses:**
    - `200 OK`: List of `Event` objects.

#### Get Event by ID
- **Endpoint:** `GET /event/{id}`
- **Method:** `GET`
- **Purpose:** Retrieves specific details for an event.
- **Parameters:**
    - `id` (Path, Integer): The event ID. [Required]
- **Responses:**
    - `200 OK`: `Event` object.

#### Create Event
- **Endpoint:** `POST /event`
- **Method:** `POST`
- **Purpose:** Creates a new individual event.
- **Parameters:**
    - **Body (JSON):** `Event` object.
- **Responses:**
    - `200 OK`: Event created.

#### Update Event
- **Endpoint:** `PUT /event/{id}`
- **Method:** `PUT`
- **Purpose:** Modifies an existing event.
- **Parameters:**
    - `id` (Path, int): The event ID. [Required]
    - **Body (JSON):** `Event` object.
- **Responses:**
    - `200 OK`: Event updated.

#### Delete Event
- **Endpoint:** `DELETE /event/{id}`
- **Method:** `DELETE`
- **Purpose:** Deletes an event.
- **Parameters:**
    - `id` (Path, Integer): The event ID. [Required]
- **Responses:**
    - `200 OK`: Event deleted.

---

## Event Categories
**Base URL:** `/eventCategory`

#### List All Categories
- **Endpoint:** `GET /eventCategory`
- **Method:** `GET`
- **Purpose:** Retrieves all defined event categories.
- **Responses:**
    - `200 OK`: List of `EventCategory` objects.

#### Get Category by ID
- **Endpoint:** `GET /eventCategory/{id}`
- **Method:** `GET`
- **Purpose:** Retrieves details for a specific category.
- **Parameters:**
    - `id` (Path, int): The category ID. [Required]
- **Responses:**
    - `200 OK`: `EventCategory` object.

#### Create Category
- **Endpoint:** `POST /create`
- **Method:** `POST`
- **Purpose:** Creates a new event category with recurring scheduling options.
- **Parameters:**
    - **Body (JSON):** `CreateEventCategoryRequest`
        - `name` (String): Category name. [Required]
        - `startingTime` (Time): event start time (HH:MM:SS). [Required]
        - `endTime` (Time): event end time. [Required]
        - `attendanceTimeStart` (Time): Time when marking attendance becomes available. [Required]
        - `attendanceDuration` (Integer): Duration in minutes for the attendance window. [Required, Min: 5]
        - `repeatable` (Boolean): Whether the event repeats.
        - `repeatableType` (String): Type of repetition (e.g., DAILY, WEEKLY).
        - `repeatsOnSpecificDay` (String): Specific day for weekly repetition.
        - `baseDate` (LocalDate): The initial date for the schedule.
- **Responses:**
    - `200 OK`: Category created.

#### Update Category
- **Endpoint:** `PUT /{id}`
- **Method:** `PUT`
- **Purpose:** Updates an existing event category structure.
- **Parameters:**
    - `id` (Path, int): The category ID. [Required]
    - **Body (JSON):** `UpdateEventCategoryRequest` (Similar fields to Create).
- **Responses:**
    - `200 OK`: Category updated.

#### Delete Category
- **Endpoint:** `DELETE /{id}`
- **Method:** `DELETE`
- **Purpose:** Deletes an event category and its configurations.
- **Parameters:**
    - `id` (Path, int): The category ID. [Required]
- **Responses:**
    - `200 OK`: Category deleted.
