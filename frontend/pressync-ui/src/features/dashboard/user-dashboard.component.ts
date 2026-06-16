import { Component, OnInit, inject, signal, effect } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { AttendanceService, AttendanceRecord, MarkState } from '../../app/core/services/attendance.service';
import { AuthService } from '../../app/core/auth/auth';
import { EventCategoryService, EventCategory } from '../../app/core/services/event-category.service';
import { UserService, UserGetAllDTO } from '../../app/core/services/user.service';

@Component({
  selector: 'app-user-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.scss'],
  providers: [DatePipe]
})
export class UserDashboardComponent implements OnInit {
  private attendanceService = inject(AttendanceService);
  private authService = inject(AuthService);
  private eventCategoryService = inject(EventCategoryService);
  private userService = inject(UserService);

  currentUser = signal<UserGetAllDTO | null>(null);

  readonly markState = this.attendanceService.markState;
  attendanceHistory: AttendanceRecord[] = [];
  errorMessage = '';

  todaySchedule = signal<EventCategory[]>([]);
  scheduleLoading = signal<boolean>(true);
  scheduleError = signal<string>('');
  scheduleOpen = signal(true);

  toggleSchedule(): void {
    this.scheduleOpen.update(v => !v);
  }

  constructor() {
    effect(() => {
      this.eventCategoryService.scheduleRefreshTrigger();
      this.fetchTodaySchedule();
    });
  }

  ngOnInit(): void {
    this.fetchCurrentUser();
    this.fetchHistory();
    this.fetchTodaySchedule();
  }

  private fetchCurrentUser(): void {
    const userId = this.authService.getUserId();
    if (userId) {
      this.userService.getUserById(userId).subscribe({
        next: (user) => this.currentUser.set(user),
        error: () => this.currentUser.set(null)
      });
    }
  }

  private fetchTodaySchedule(): void {
    this.scheduleLoading.set(true);
    this.eventCategoryService.getTodaySchedule().subscribe({
      next: (schedule) => {
        this.todaySchedule.set(schedule);
        this.scheduleLoading.set(false);
      },
      error: () => {
        this.scheduleError.set('Unable to load today\'s schedule.');
        this.scheduleLoading.set(false);
      }
    });
  }

  repeatTypeLabel(type: string): string {
    const map: Record<string, string> = {
      DAILY: 'Every Day',
      WEEKLY: 'Every Week',
      BIWEEKLY: 'Every 2 Weeks',
      MONTHLY: 'Every Month',
      YEARLY: 'Every Year',
    };
    return map[type] ?? type;
  }

  private fetchHistory(): void {
    const userId = this.authService.getUserId();

    if (!userId) {
      this.errorMessage = 'Could not determine user identity from session.';
      return;
    }

    this.attendanceService.getUserAttendance(userId).subscribe({
      next: (history) => {
        this.attendanceHistory = history;
      },
      error: () => {
        this.errorMessage = 'Failed to load attendance history. Please try again later.';
      }
    });
  }
}
