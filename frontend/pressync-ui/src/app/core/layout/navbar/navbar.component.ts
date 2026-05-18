import { Component, computed, inject, signal } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators';
import { AuthService } from '../../auth/auth';
import { CategoryCreateComponent } from '../../../../features/admin/category-create/category-create.component';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, CommonModule, CategoryCreateComponent],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  private router = inject(Router);
  readonly authService = inject(AuthService);

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
      .subscribe(() => this.currentUrl.set(this.router.url));
  }

  onLogout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  openCategoryModal(): void {
    this.showCategoryModal.set(true);
  }

  closeCategoryModal(): void {
    this.showCategoryModal.set(false);
  }

  onCategoryCreated(): void {
    this.closeCategoryModal();
  }
}
