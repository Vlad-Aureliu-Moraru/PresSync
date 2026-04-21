import { Routes } from '@angular/router';
import { LoginComponent } from '../features/auth/login/login';
import { RegisterComponent } from '../features/auth/register/register';
import { StudentDashboardComponent } from '../features/dashboard/student-dashboard.component';
import { CategorySearchComponent } from '../features/categories/category-search/category-search.component';
import { CategoryStatsComponent } from '../features/categories/category-stats/category-stats.component';
import { UserListComponent } from '../features/admin/user-list/user-list.component';
import { UserAttendanceComponent } from '../features/admin/user-attendance/user-attendance.component';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'student-dashboard', component: StudentDashboardComponent },
  { path: 'search', component: CategorySearchComponent },
  { path: 'category-stats/:id', component: CategoryStatsComponent },
  { path: 'admin/users', component: UserListComponent, canActivate: [roleGuard] },
  { path: 'admin/users/:id/attendance', component: UserAttendanceComponent, canActivate: [roleGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' }
];
