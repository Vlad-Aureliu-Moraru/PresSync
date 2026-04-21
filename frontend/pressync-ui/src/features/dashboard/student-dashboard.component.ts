import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { AttendanceService, AttendanceRecord } from '../../app/core/services/attendance.service';
import { AuthService } from '../../app/core/auth/auth';
import { EventCategoryService, EventCategory } from '../../app/core/services/event-category.service';
import { UserService, UserGetAllDTO } from '../../app/core/services/user.service';
import { CategoryCreateComponent } from '../admin/category-create/category-create.component';

@Component({
  selector: 'app-student-dashboard',
  standalone: true,
  imports: [CommonModule, CategoryCreateComponent],
  templateUrl: './student-dashboard.component.html',
  styleUrls: ['./student-dashboard.component.scss'],
  providers: [DatePipe]
})
export class StudentDashboardComponent implements OnInit {
  private attendanceService = inject(AttendanceService);
  private authService = inject(AuthService);
  private eventCategoryService = inject(EventCategoryService);
  private userService = inject(UserService);

  currentUser = signal<UserGetAllDTO | null>(null);
  authServiceInject = this.authService;
  showCategoryModal = signal(false);

  isLoading = true;
  isMarkedPresent = false;
  attendanceHistory: AttendanceRecord[] = [];
  errorMessage = '';

  // Today's schedule state
  todaySchedule = signal<EventCategory[]>([]);
  scheduleLoading = signal<boolean>(true);
  scheduleError = signal<string>('');

  ngOnInit(): void {
    this.isLoading = true;
    
    // Fetch current user profile
    this.fetchCurrentUser();

    // Attempt to automatically mark attendance
    this.attendanceService.markAttendance().subscribe({
      next: () => {
        this.isLoading = false;
        this.isMarkedPresent = true;
      },
      error: () => {
        // If marking fails, fallback to fetching history
        this.isMarkedPresent = false;
        this.fetchHistory();
      }
    });

    // Fetch Today's Schedule
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

  openModal(): void {
    this.showCategoryModal.set(true);
  }

  closeModal(): void {
    this.showCategoryModal.set(false);
  }

  onCategoryCreated(): void {
    this.closeModal();
    this.fetchTodaySchedule();
  }
}
