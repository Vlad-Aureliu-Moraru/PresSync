import { Routes } from '@angular/router';
import { LoginComponent } from '../features/auth/login/login';
import { RegisterComponent } from '../features/auth/register/register';
import { StudentDashboardComponent } from '../features/dashboard/student-dashboard.component';
import { CategorySearchComponent } from '../features/categories/category-search/category-search.component';
import { CategoryStatsComponent } from '../features/categories/category-stats/category-stats.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'student-dashboard', component: StudentDashboardComponent },
  { path: 'search', component: CategorySearchComponent },
  { path: 'category-stats/:id', component: CategoryStatsComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' }
];
