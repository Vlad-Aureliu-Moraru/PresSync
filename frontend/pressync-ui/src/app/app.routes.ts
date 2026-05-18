import { Routes } from '@angular/router';
import { LoginComponent } from '../features/auth/login/login';
import { RegisterComponent } from '../features/auth/register/register';
import { MfaValidateComponent } from '../features/auth/mfa-validate/mfa-validate';
import { UserDashboardComponent } from '../features/dashboard/user-dashboard.component';
import { SuperiorDashboardComponent } from '../features/dashboard/superior-dashboard.component';
import { CategorySearchComponent } from '../features/categories/category-search/category-search.component';
import { CategoryStatsComponent } from '../features/categories/category-stats/category-stats.component';
import { UserListComponent } from '../features/admin/user-list/user-list.component';
import { UserAttendanceComponent } from '../features/admin/user-attendance/user-attendance.component';
import { NotificationHistoryComponent } from '../features/notifications/notification-history/notification-history.component';
import { roleGuard } from './core/guards/role.guard';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'mfa-validate', component: MfaValidateComponent },
  { path: 'user-dashboard', component: UserDashboardComponent, canActivate: [authGuard] },
  { path: 'superior-dashboard', component: SuperiorDashboardComponent, canActivate: [roleGuard] },
  { path: 'search', component: CategorySearchComponent },
  { path: 'category-stats/:id', component: CategoryStatsComponent },
  { path: 'admin', redirectTo: '/admin/users', pathMatch: 'full' },
  { path: 'admin/users', component: UserListComponent, canActivate: [roleGuard] },
  { path: 'admin/users/:id/attendance', component: UserAttendanceComponent, canActivate: [roleGuard] },
  { path: 'notifications', component: NotificationHistoryComponent, canActivate: [authGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' }
];
