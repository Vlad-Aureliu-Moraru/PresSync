import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../app/core/auth/auth';
import { UserService, UserGetAllDTO } from '../../app/core/services/user.service';
import { EventCategoryService, EventCategory } from '../../app/core/services/event-category.service';

@Component({
  selector: 'app-superior-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './superior-dashboard.component.html',
  styleUrls: ['./superior-dashboard.component.scss']
})
export class SuperiorDashboardComponent implements OnInit {
  private authService = inject(AuthService);
  private userService = inject(UserService);
  private eventCategoryService = inject(EventCategoryService);
  private router = inject(Router);

  currentUser = signal<UserGetAllDTO | null>(null);
  users = signal<UserGetAllDTO[]>([]);
  filteredUsers = signal<UserGetAllDTO[]>([]);
  searchQuery = signal('');
  isLoadingUsers = signal(true);
  usersError = signal('');

  todaySchedule = signal<EventCategory[]>([]);
  scheduleLoading = signal(true);
  scheduleError = signal('');

  ngOnInit(): void {
    this.fetchCurrentUser();
    this.fetchUsers();
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

  private fetchUsers(): void {
    this.isLoadingUsers.set(true);
    this.userService.getAllUsers().subscribe({
      next: (data) => {
        this.users.set(data);
        this.applyFilter();
        this.isLoadingUsers.set(false);
      },
      error: () => {
        this.usersError.set('Failed to load users.');
        this.isLoadingUsers.set(false);
      }
    });
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

  onSearch(query: string): void {
    this.searchQuery.set(query);
    this.applyFilter();
  }

  private applyFilter(): void {
    const q = this.searchQuery().toLowerCase();
    if (!q) {
      this.filteredUsers.set(this.users());
      return;
    }
    this.filteredUsers.set(
      this.users().filter(u =>
        u.name.toLowerCase().includes(q) ||
        u.surname.toLowerCase().includes(q) ||
        u.email.toLowerCase().includes(q) ||
        u.role.toLowerCase().includes(q)
      )
    );
  }

  viewHistory(userId: number): void {
    this.router.navigate(['/admin/users', userId, 'attendance']);
  }
}
