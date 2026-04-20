import { Routes } from '@angular/router';
import { LoginComponent } from '../features/auth/login/login';
import { RegisterComponent } from '../features/auth/register/register';
import { StudentDashboardComponent } from '../features/dashboard/student-dashboard.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'student-dashboard', component: StudentDashboardComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' }
];
