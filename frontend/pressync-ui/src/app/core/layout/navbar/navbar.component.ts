import { Component, computed, inject, signal } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators';
import { AuthService } from '../../auth/auth';
import { AttendanceService } from '../../services/attendance.service';
import { CategoryCreateComponent } from '../../../../features/admin/category-create/category-create.component';
import { NotificationService } from '../../../shared/services/notification.service';
import { NotificationPanelComponent } from '../../../shared/components/notification-panel/notification-panel.component';
@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, CommonModule, CategoryCreateComponent, NotificationPanelComponent],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  private router = inject(Router);
  readonly authService = inject(AuthService);
  readonly notificationService = inject(NotificationService);
  private attendanceService = inject(AttendanceService);

  isNotificationPanelOpen = signal(false);
  isMobileMenuOpen = signal(false);

  private currentUrl = signal(this.router.url);
  readonly isAuthPage = computed(() => {
    const url = this.currentUrl();
    return url.includes('/login') || url.includes('/register') || url.includes('/mfa-validate');
  });

  readonly dashboardLink = computed(() => {
    if (this.authService.isAdminOrModerator()) {
      return '/superior-dashboard';
    }
    return '/user-dashboard';
  });

  showCategoryModal = signal(false);

  constructor() {
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe(() => {
        this.currentUrl.set(this.router.url);
        this.closeMobileMenu();
      });
  }

  onLogout(): void {
    this.attendanceService.stopMonitoring();
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  toggleNotificationPanel(): void {
    this.isNotificationPanelOpen.update((v) => !v);
  }

  closeNotificationPanel(): void {
    this.isNotificationPanelOpen.set(false);
  }

  toggleMobileMenu(): void {
    this.isMobileMenuOpen.update((v) => !v);
  }

  closeMobileMenu(): void {
    this.isMobileMenuOpen.set(false);
  }

  openCategoryModal(): void {
    this.closeNotificationPanel();
    this.showCategoryModal.set(true);
  }

  closeCategoryModal(): void {
    this.showCategoryModal.set(false);
  }

  onCategoryCreated(): void {
    this.closeCategoryModal();
  }
}
