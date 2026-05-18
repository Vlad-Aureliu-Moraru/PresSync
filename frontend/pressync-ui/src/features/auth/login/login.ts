import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../app/core/auth/auth';
import { environment } from '../../../environments/environment';

interface LoginResponse {
  token?: string;
  requiresMfa?: boolean;
  otpDestination?: string;
}

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);
  private authService = inject(AuthService);

  loginForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]]
  });

  errorMessage = '';
  isLoading = false;

  get email() { return this.loginForm.controls.email; }
  get password() { return this.loginForm.controls.password; }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.http.post<LoginResponse>(`${environment.apiUrl}/auth/login`, this.loginForm.getRawValue())
      .subscribe({
        next: (res) => {
          this.isLoading = false;

          if (res.requiresMfa) {
            sessionStorage.setItem('pressync_mfa_data', JSON.stringify({
              email: this.loginForm.getRawValue().email,
              otpDestination: res.otpDestination ?? ''
            }));
            this.router.navigate(['/mfa-validate']);
            return;
          }

          if (!res.token) {
            this.errorMessage = 'Login response is missing access token.';
            return;
          }

          this.authService.setToken(res.token);
          this.navigateByRole();
        },
        error: (err: HttpErrorResponse) => {
          this.isLoading = false;
          if (err.status === 401) {
            this.errorMessage = 'Invalid email or password. Please try again.';
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
