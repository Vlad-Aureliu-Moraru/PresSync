import { Component } from '@angular/core';
import { ApiError } from '../../api/api-error';
import {
  AttendanceCreateRequest,
  AttendanceDetail,
  AttendanceGet,
  AuthenticationRequest,
  CreateEventCategoryRequest,
  Event,
  EventCategory,
  EventCategoryStats,
  UpdateEventCategoryRequest,
  UserCreateRequest,
  UserGet,
} from '../../api/api.models';
import { Observable } from 'rxjs';
import { AttendanceService } from '../../api/services/attendance.service';
import { AuthService } from '../../api/services/auth.service';
import { EventCategoryService } from '../../api/services/event-category.service';
import { EventService } from '../../api/services/event.service';
import { UserService } from '../../api/services/user.service';

@Component({
  selector: 'app-api-demo',
  templateUrl: './api-demo.component.html',
})
export class ApiDemoComponent {
  private static readonly TOKEN_STORAGE_KEY = 'pressync_token';

  users: UserGet[] = [];
  categories: EventCategory[] = [];
  events: Event[] = [];
  attendance: AttendanceGet[] = [];
  attendanceByUser: AttendanceDetail[] = [];
  selectedUser: UserGet | null = null;
  selectedCategory: EventCategory | null = null;
  selectedEvent: Event | null = null;
  selectedAttendance: AttendanceDetail | null = null;
  categoryStats: EventCategoryStats | null = null;
  lastResult: unknown = null;
  loading = false;
  error: ApiError | null = null;
  token = localStorage.getItem(ApiDemoComponent.TOKEN_STORAGE_KEY);

  registerPayload: UserCreateRequest = {
    name: '',
    surname: '',
    email: '',
    password: '',
  };

  loginPayload: AuthenticationRequest = {
    email: '',
    password: '',
  };

  userIdInput = '';
  userUpdatePayload: UserCreateRequest = {
    name: '',
    surname: '',
    email: '',
    password: '',
  };

  categoryIdInput = '';
  categoryPayload: CreateEventCategoryRequest = {
    name: '',
    startingTime: '08:00:00',
    endTime: '10:00:00',
    attendanceTimeStart: '08:00:00',
    attendanceDuration: 30,
    repeatable: false,
    repeatableType: null,
    repeatsOnSpecificDay: null,
    baseDate: null,
  };
  categoryUpdatePayload: UpdateEventCategoryRequest = {
    name: '',
    startingTime: '08:00:00',
    endTime: '10:00:00',
    attendanceTimeStart: '08:00:00',
    attendanceDuration: 30,
    repeatable: false,
    repeatableType: null,
    repeatsOnSpecificDay: null,
    baseDate: null,
  };

  eventIdInput = '';
  eventPayloadText = '{\n  "eventCategory": { "id": 1 },\n  "active": false,\n  "archived": false,\n  "date": "2026-04-20"\n}';
  eventUpdatePayloadText = '{\n  "eventCategory": { "id": 1 },\n  "active": false,\n  "archived": false,\n  "date": "2026-04-21"\n}';

  attendanceIdInput = '';
  attendanceUserIdInput = '';
  attendanceCategoryIdInput = '';
  attendanceUpdatePayload: AttendanceCreateRequest = {
    userId: 0,
    eventId: 0,
  };

  constructor(
    private readonly authService: AuthService,
    private readonly userService: UserService,
    private readonly eventCategoryService: EventCategoryService,
    private readonly eventService: EventService,
    private readonly attendanceService: AttendanceService,
  ) {}

  register(): void {
    this.runRequest('auth.register', this.authService.register(this.registerPayload), (response) => {
      this.setToken(response.token);
      this.lastResult = response;
    });
  }

  login(): void {
    this.runRequest('auth.login', this.authService.login(this.loginPayload), (response) => {
      this.setToken(response.token);
      this.lastResult = response;
    });
  }

  logout(): void {
    this.setToken(null);
    this.lastResult = { message: 'Logged out' };
  }

  loadUsers(): void {
    this.runRequest('user.getAll', this.userService.getAllUsers(this.token ?? undefined), (users) => {
      this.users = users;
      this.lastResult = users;
    });
  }

  getUserById(): void {
    const id = this.parseId(this.userIdInput, 'User ID');
    if (id === null) {
      return;
    }
    this.runRequest('user.getById', this.userService.getUserById(id, this.token ?? undefined), (user) => {
      this.selectedUser = user;
      this.lastResult = user;
    });
  }

  updateUser(): void {
    const id = this.parseId(this.userIdInput, 'User ID');
    if (id === null) {
      return;
    }
    this.runRequest(
      'user.update',
      this.userService.updateUser(id, this.userUpdatePayload, this.token ?? undefined),
      () => {
        this.lastResult = { message: `User ${id} updated` };
      },
    );
  }

  deleteUser(): void {
    const id = this.parseId(this.userIdInput, 'User ID');
    if (id === null) {
      return;
    }
    this.runRequest('user.delete', this.userService.deleteUser(id, this.token ?? undefined), () => {
      this.lastResult = { message: `User ${id} deleted` };
    });
  }

  loadCategories(): void {
    this.runRequest(
      'eventCategory.getAll',
      this.eventCategoryService.getAllCategories(this.token ?? undefined),
      (categories) => {
        this.categories = categories;
        this.lastResult = categories;
      },
    );
  }

  getCategoryById(): void {
    const id = this.parseId(this.categoryIdInput, 'Category ID');
    if (id === null) {
      return;
    }
    this.runRequest(
      'eventCategory.getById',
      this.eventCategoryService.getCategoryById(id, this.token ?? undefined),
      (category) => {
        this.selectedCategory = category;
        this.lastResult = category;
      },
    );
  }

  createCategory(): void {
    this.runRequest(
      'eventCategory.create',
      this.eventCategoryService.createCategory(this.categoryPayload, this.token ?? undefined),
      (result) => {
        this.lastResult = result;
      },
    );
  }

  updateCategory(): void {
    const id = this.parseId(this.categoryIdInput, 'Category ID');
    if (id === null) {
      return;
    }
    this.runRequest(
      'eventCategory.update',
      this.eventCategoryService.updateCategory(id, this.categoryUpdatePayload, this.token ?? undefined),
      () => {
        this.lastResult = { message: `Category ${id} updated` };
      },
    );
  }

  deleteCategory(): void {
    const id = this.parseId(this.categoryIdInput, 'Category ID');
    if (id === null) {
      return;
    }
    this.runRequest(
      'eventCategory.delete',
      this.eventCategoryService.deleteCategory(id, this.token ?? undefined),
      () => {
        this.lastResult = { message: `Category ${id} deleted` };
      },
    );
  }

  loadEvents(): void {
    this.runRequest('event.getAll', this.eventService.getAllEvents(this.token ?? undefined), (events) => {
      this.events = events;
      this.lastResult = events;
    });
  }

  getEventById(): void {
    const id = this.parseId(this.eventIdInput, 'Event ID');
    if (id === null) {
      return;
    }
    this.runRequest('event.getById', this.eventService.getEventById(id, this.token ?? undefined), (event) => {
      this.selectedEvent = event;
      this.lastResult = event;
    });
  }

  createEvent(): void {
    const payload = this.parseJson<Event>(this.eventPayloadText, 'Create event payload');
    if (!payload) {
      return;
    }
    this.runRequest('event.create', this.eventService.createEvent(payload, this.token ?? undefined), (result) => {
      this.lastResult = result;
    });
  }

  updateEvent(): void {
    const id = this.parseId(this.eventIdInput, 'Event ID');
    if (id === null) {
      return;
    }
    const payload = this.parseJson<Event>(this.eventUpdatePayloadText, 'Update event payload');
    if (!payload) {
      return;
    }
    this.runRequest('event.update', this.eventService.updateEvent(id, payload, this.token ?? undefined), () => {
      this.lastResult = { message: `Event ${id} updated` };
    });
  }

  deleteEvent(): void {
    const id = this.parseId(this.eventIdInput, 'Event ID');
    if (id === null) {
      return;
    }
    this.runRequest('event.delete', this.eventService.deleteEvent(id, this.token ?? undefined), () => {
      this.lastResult = { message: `Event ${id} deleted` };
    });
  }

  loadAttendance(): void {
    this.runRequest(
      'attendance.getAll',
      this.attendanceService.getAllAttendance(this.token ?? undefined),
      (attendance) => {
        this.attendance = attendance;
        this.lastResult = attendance;
      },
    );
  }

  getAttendanceById(): void {
    const id = this.parseId(this.attendanceIdInput, 'Attendance ID');
    if (id === null) {
      return;
    }
    this.runRequest(
      'attendance.getById',
      this.attendanceService.getAttendanceById(id, this.token ?? undefined),
      (attendance) => {
        this.selectedAttendance = attendance;
        this.lastResult = attendance;
      },
    );
  }

  getAttendanceByUser(): void {
    const userId = this.parseId(this.attendanceUserIdInput, 'Attendance User ID');
    if (userId === null) {
      return;
    }
    this.runRequest(
      'attendance.getByUser',
      this.attendanceService.getAttendanceByUser(userId, this.token ?? undefined),
      (attendance) => {
        this.attendanceByUser = attendance;
        this.lastResult = attendance;
      },
    );
  }

  markAttendance(): void {
    this.runRequest('attendance.mark', this.attendanceService.markAttendance(this.token ?? undefined), (result) => {
      this.lastResult = result;
    });
  }

  updateAttendance(): void {
    const id = this.parseId(this.attendanceIdInput, 'Attendance ID');
    if (id === null) {
      return;
    }
    this.runRequest(
      'attendance.update',
      this.attendanceService.updateAttendance(id, this.attendanceUpdatePayload, this.token ?? undefined),
      (result) => {
        this.lastResult = result;
      },
    );
  }

  getCategoryStats(): void {
    const categoryId = this.parseId(this.attendanceCategoryIdInput, 'Category ID (stats)');
    if (categoryId === null) {
      return;
    }
    this.runRequest(
      'attendance.getCategoryStats',
      this.attendanceService.getCategoryStats(categoryId, this.token ?? undefined),
      (stats) => {
        this.categoryStats = stats;
        this.lastResult = stats;
      },
    );
  }

  private runRequest<T>(
    operation: string,
    request: Observable<T>,
    onSuccess: (data: T) => void,
  ): void {
    this.loading = true;
    this.error = null;
    request.subscribe({
      next: (data) => {
        onSuccess(data);
        this.loading = false;
      },
      error: (error: ApiError) => {
        this.error = {
          ...error,
          operation: error.operation || operation,
        };
        this.loading = false;
      },
    });
  }

  private parseId(value: string, label: string): number | null {
    const parsed = Number(value);
    if (!Number.isInteger(parsed) || parsed <= 0) {
      this.error = {
        status: 400,
        message: `${label} must be a positive integer`,
        operation: 'validation',
      };
      return null;
    }
    return parsed;
  }

  private parseJson<T>(value: string, label: string): T | null {
    try {
      return JSON.parse(value) as T;
    } catch {
      this.error = {
        status: 400,
        message: `${label} is not valid JSON`,
        operation: 'validation',
      };
      return null;
    }
  }

  private setToken(token: string | null): void {
    this.token = token;
    if (token) {
      localStorage.setItem(ApiDemoComponent.TOKEN_STORAGE_KEY, token);
      return;
    }
    localStorage.removeItem(ApiDemoComponent.TOKEN_STORAGE_KEY);
  }
}
