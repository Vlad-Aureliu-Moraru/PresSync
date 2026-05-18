import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { AttendanceService, AttendanceRecord } from '../../app/core/services/attendance.service';
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

  isLoading = true;
  isMarkedPresent = false;
  attendanceHistory: AttendanceRecord[] = [];
  errorMessage = '';

  todaySchedule = signal<EventCategory[]>([]);
  scheduleLoading = signal<boolean>(true);
  scheduleError = signal<string>('');

  ngOnInit(): void {
    this.isLoading = true;

    this.fetchCurrentUser();

    this.attendanceService.markAttendance().subscribe({
      next: () => {
        this.isLoading = false;
        this.isMarkedPresent = true;
      },
      error: () => {
        this.isMarkedPresent = false;
        this.fetchHistory();
      }
    });

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

  private fetchHistory(): void {
    const userId = this.authService.getUserId();

    if (!userId) {
      this.errorMessage = 'Could not determine user identity from session.';
      this.isLoading = false;
      return;
    }

    this.attendanceService.getUserAttendance(userId).subscribe({
      next: (history) => {
        this.attendanceHistory = history;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load attendance history. Please try again later.';
        this.isLoading = false;
      }
    });
  }
}
