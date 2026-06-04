# Attendance Module

## How it works
This module handles all operations related to user attendance at specific events.
It follows the CQRS (Command Query Responsibility Segregation) pattern,
separating read operations (QueryHandlers) from write operations (CommandHandler).
Operations are exposed via the `AttendanceController` and data is persisted using the `AttendanceRepository`.

## Why it is structured this way
The CQRS pattern is used here to decouple the read and write logic, which allows for better scalability,
easier maintenance, and clearer separation of concerns.
This is particularly useful as attendance logic might require different scaling profiles for read
(e.g. checking statistics) vs write (e.g. marking present).

## Connection to other components
- **User Module**: Associates attendance records with specific users.
- **Event Module**: Links attendance records to specific events that are occurring.
- **EventCategory Module**: Connects to the aggregated attendance statistics for event categories.

public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();
}
public class AttendanceController {
    private final CreateAttendanceCommand createAttendanceCommand;
    private final UpdateAttendanceCommand updateAttendanceCommand;
    private final GetAllAttendanceQuery getAllAttendanceQuery;
    private final GetAttendanceByIdQuery getAttendanceByIdQuery;
    private final GetAttendanceByUserIdQuery getAttendanceByUserIdQuery;
    private final GetEventCategoryStatsQuery getEventCategoryStatsQuery;

    @GetMapping("/stats/category/{categoryId}")
    public ResponseEntity<EventCategoryStatsDTO> getEventCategoryStats(@PathVariable int categoryId) {
        return getEventCategoryStatsQuery.execute(categoryId);
    }

    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        return getAllAttendanceQuery.execute(null);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable  int id) {
        return getAttendanceByIdQuery.execute(id);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Attendance>> getAttendanceByUserID (@PathVariable  int userId) {
        return getAttendanceByUserIdQuery.execute(userId);
    }
    @PostMapping("/mark")
    public ResponseEntity<String> createAttendance(@AuthenticationPrincipal User user) {
        return createAttendanceCommand.execute(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateAttendanceById(@PathVariable int id, @RequestBody AttendanceCreateDTO attendance) {
        AttendanceUpdateDTO attendanceUpdateDTO = new AttendanceUpdateDTO(id, attendance);
        return updateAttendanceCommand.execute(attendanceUpdateDTO);
    }
}

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


# Event Module

## How it works
This module handles individual events that are instances of event categories.
It manages the creation, retrieval, and updating of events. Using the CQRS pattern,
`CommandHandlers` handle event creation and modification, while `QueryHandlers` handle
searching and retrieving event details. `EventController` exposes these as REST endpoints,
and `EventRepository` interacts with the database.

## Why it is structured this way
The use of CQRS ensures that complex logic for generating recurring events or updating existing events
is separated from simple read operations. This makes the codebase cleaner and more testable.

## Connection to other components
- **EventCategory Module**: Every event belongs to an `EventCategory`. Events are essentially occurrences spawned based on the configuration of their category.
- **Attendance Module**: Events are the target of attendance records. Users are marked as attended or absent for specific events.
- **Common Module (Scheduling)**: The schedulers in the common module automatically generate these events based on configuration.


public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    private EventCategory eventCategory;
    @Column(name = "active")
    private Boolean active;
    @Column(name = "archived")
    private Boolean archived;
    @Column(name = "date")
    private LocalDate date;

}

public class EventController {
    private final CreateEventCommand createEventCommand;
    private final DeleteEventCommand deleteEventCommand;
    private final UpdateEventCommand updateEventCommand;
    private final GetAllEventsQuery getAllEventsQuery;
    private final GetEventByIdQuery getEventByIdQuery;


    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(){
        return getAllEventsQuery.execute(null);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Integer id){
        return  getEventByIdQuery.execute(id);

    }

    @PostMapping
    public ResponseEntity<String> createEvent(@RequestBody Event event){
        return createEventCommand.execute(event);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteEvent(@PathVariable Integer id){
        return deleteEventCommand.execute(id);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable int id,@RequestBody Event event){
        EventDTO eventDTO = new EventDTO(id, event);
        updateEventCommand.execute(eventDTO);
        return ResponseEntity.ok().build();
    }

}


# EventCategory Module

## How it works
This module manages the categories/templates for events (e.g., a "Math Class" or a "Weekly Meeting").
It handles creating and updating event categories using `CommandHandlers` and retrieving them via `QueryHandlers`.
The `EventCategoryController` provides the API interface, whilst the `EventCategoryRepository` stores the data.

## Why it is structured this way
By treating `EventCategory` as a distinct entity from `Event`,
the system can define templates and rules for recurring events without
needing to duplicate scheduling logic manually for every occurrence.

## Connection to other components
- **EventCategoryConfig Module**: Defines the complex recurring rules and configurations for how an event category should spawn individual events.
- **Event Module**: Acts as the template from which individual `Event` instances are instantiated.
- **Attendance Module**: Statistics query handlers within the Attendance module calculate metrics dynamically based on the event category.

public class EventCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "startingTime")
    private Time startingTime;

    @Column(name = "endTime")
    private Time endTime;

    @Column(name = "attendanceTimeStart")
    private Time attendanceTimeStart;

    @Column(name = "attendanceDuration")
    @NotNull(message = "Duration is required")
    private Integer attendanceDuration;

    @Column(name = "repeatable")
    private Boolean repeatable;

    @Column(name = "date")
    private LocalDate specificDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "config_id")
    private EventCategoryConfig categoryConfig;

    @OneToMany(mappedBy = "eventCategory")
    @JsonIgnore
    private List<Event> events;
}

public class EventCategoryController {
    private final CreateEventCategoryCommand createEventCategoryCommand;
    private final GetAllEventCategoriesQuerry getAllEventCategoriesQuerry;
    private final GetEventCategoryQuery  getEventCategoryQuery;
    private final DeleteEventCategoryCommand deleteEventCategoryCommand;
    private final UpdateEventCategoryCommand updateEventCategoryCommand;


    @GetMapping
    public ResponseEntity<List<EventCategory>> getAllEventCategories(){
        return getAllEventCategoriesQuerry.execute(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventCategory> getEventCategory(@PathVariable int id){
        return getEventCategoryQuery.execute(id);
    }
    @PostMapping("/create")
    public ResponseEntity<String> addEventCategory(@RequestBody CreateEventCategoryRequest eventCategoryRequest){
      return   createEventCategoryCommand.execute(eventCategoryRequest);

    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateEventCategory(@PathVariable int id, @RequestBody UpdateEventCategoryRequest request){
        EventCategoryUpdateDTO eventCategoryUpdateDTO = new EventCategoryUpdateDTO(id, request);
        return updateEventCategoryCommand.execute(eventCategoryUpdateDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> deleteEventCategory(@PathVariable int id){
        return deleteEventCategoryCommand.execute(id);
    }
}


# EventCategoryConfig Module

## How it works
This module encapsulates the configuration and rules (like recurrence, start time, and time intervals) for `EventCategory` entities.
It manages data regarding when and how often an event category should trigger individual events.

## Why it is structured this way
Abstracting the configuration into its own module/entity simplifies the `EventCategory` model. It allows for complex configurations
(like cron expressions, repeatable rules, and active periods) to be decoupled, making it easier to manage, validate,
and update scheduling logic independently from the base category details.

## Connection to other components
- **EventCategory Module**: Directly linked to an `EventCategory`, acting as its rules engine.
- **Common Module (Schedulers)**: Serves as the source of truth for the daily and minute-based schedulers that automatically instantiate events.

public class EventCategoryConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "repeatableType")
    private RepeatableType repeatableType;

    @Enumerated(EnumType.STRING)
    @Column(name = "repeatsOnSpecificDay")
    private RepeatsOnSpecificDay repeatsOnSpecificDay;

    @Column(name ="base_date")
    private LocalDate baseDate;

    @OneToMany(mappedBy = "categoryConfig", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<EventCategory> categories;
}


# Services Module

## How it works
This module contains cross-cutting domain services that aren't strict data access or application-level CQRS handlers. Specifically, it holds authentication infrastructure (`Auth`)—such as JWT generation, parsing, and validation—and custom exception handling logic (`Exceptions`) for centralized error formatting.

## Why it is structured this way
By isolating these common services, other modules (like User and Config) can depend on a shared library of logic. This prevents code duplication (like JWT parsing logic) across different controllers or handlers. Exception formatting is centralized to maintain a consistent API response structure for clients.

## Connection to other components
- **User Module**: Employed heavily by the `AuthenticationController` to issue and validate tokens upon login/registration.
- **Config Module**: The security configurations use components from this Auth service layer to intercept and authenticate requests dynamically.
- System-wide: The defined exceptions are thrown consistently across the entire application's handlers.


# User Module

## How it works
This module manages user accounts, authentication, and authorization within the application. It includes standard user CRUD operations (via CQRS Handlers) and authentication endpoints for logging in and registering via `AuthenticationController` and `UserController`. It also contains validation logic for user credentials and details.

## Why it is structured this way
Separating user management and authentication into its own module provides a centralized location for security principles. Using CQRS for user creation and retrieval prevents the security logic from becoming tangled with general application logic.

## Connection to other components
- **Config Module**: Integrates strongly with `SecurityConfig` to provide bounded access and session management (like generating/validating JWTs).
- **Attendance Module**: Forms the basis of an attendance record—an attendance entry links a specific user to an event.
- **Services (Auth)**: Utilizes the authentication service layer to verify credentials securely.

public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRoles role = UserRoles.USER;

    @Column(name = "active", nullable = false)
    private Boolean active = true;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}

public class UserController {
    private final CreateUserCommand createUserCommand;
    private final GetAllUsersQuery getAllUsersQuery;
    private final UpdateUserCommand updateUserCommand;
    private final DeleteUserCommand deleteUserCommand;
    private final GetUserByIdQuery getUserByIdQuery;


    @GetMapping
    public ResponseEntity<List<UserGetAllDTO>> getAllUsers() {
        return getAllUsersQuery.execute(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetAllDTO>getUserById(@PathVariable Integer id) {
        return getUserByIdQuery.execute(id);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id) {
        return deleteUserCommand.execute(id);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody UserCreateDTO user) {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(id, user);
        return updateUserCommand.execute(userUpdateDTO);

    }
}
public class CreateUserCommand implements Command<UserCreateDTO, AuthenticationResponse> { // Change return type
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final UserRepository userRepository;
    private final JWTService jwtService;


    @Override
    public ResponseEntity<AuthenticationResponse> execute(UserCreateDTO entity) {
        userValidator.validate(entity.getName(), entity.getSurname(), entity.getEmail());

        User userToSave = new User();
        userToSave.setName(entity.getName());
        userToSave.setEmail(entity.getEmail());
        userToSave.setSurname(entity.getSurname());
        userToSave.setActive(true);
        userToSave.setRole(UserRoles.USER);

        userToSave.setPassword(passwordEncoder.encode(entity.getPassword()));

        userRepository.save(userToSave);

        String token = jwtService.generateToken(userToSave);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserCreateDTO user) {
        return ResponseEntity.ok(service.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
