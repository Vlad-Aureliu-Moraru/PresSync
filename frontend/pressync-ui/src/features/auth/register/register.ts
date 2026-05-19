import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../app/core/auth/auth';
import { AttendanceService } from '../../../app/core/services/attendance.service';
import { environment } from '../../../environments/environment';

interface RegisterResponse {
  token?: string;
  requiresMfa?: boolean;
  otpDestination?: string;
}

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrls: ['./register.scss']
})
export class RegisterComponent {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);
  private authService = inject(AuthService);
  private attendanceService = inject(AttendanceService);

  registerForm = this.fb.nonNullable.group({
    name: ['', [Validators.required]],
    surname: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  errorMessage = '';
  isLoading = false;

  get name() { return this.registerForm.controls.name; }
  get surname() { return this.registerForm.controls.surname; }
  get email() { return this.registerForm.controls.email; }
  get password() { return this.registerForm.controls.password; }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.http.post<RegisterResponse>(`${environment.apiUrl}/auth/register`, this.registerForm.getRawValue())
      .subscribe({
        next: (res) => {
          this.isLoading = false;

          if (res.requiresMfa) {
            sessionStorage.setItem('pressync_mfa_data', JSON.stringify({
              email: this.registerForm.getRawValue().email,
              otpDestination: res.otpDestination ?? ''
            }));
            this.router.navigate(['/mfa-validate']);
            return;
          }

          if (!res.token) {
            this.errorMessage = 'Registration response is missing access token.';
            return;
          }

          this.authService.setToken(res.token);
          this.attendanceService.startMonitoring();
          this.navigateByRole();
        },
        error: (err: HttpErrorResponse) => {
          this.isLoading = false;
          if (err.status === 400) {
            this.errorMessage = err.error?.message || 'Invalid registration data. Please check your inputs.';
          } else if (err.status === 409) {
            this.errorMessage = 'An account with this email already exists.';
          } else if (err.status === 0) {
            this.errorMessage = 'Unable to connect to the server. Please check your connection.';
          } else {
            this.errorMessage = 'An unexpected error occurred. Please try again later.';
          }
        }
      });
  }

  private navigateByRole(): void {
    const role = this.authService.getUserRole();
    if (role === 'ADMIN' || role === 'MODERATOR') {
      this.router.navigate(['/superior-dashboard']);
    } else {
      this.router.navigate(['/user-dashboard']);
    }
  }
}
