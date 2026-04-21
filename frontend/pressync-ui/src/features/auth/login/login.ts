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
}

interface VerifyOtpResponse {
  token?: string;
  accessToken?: string;
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

  otpForm = this.fb.nonNullable.group({
    otpCode: ['', [Validators.required]]
  });

  errorMessage = '';
  isLoading = false;
  step = signal<'LOGIN' | 'OTP'>('LOGIN');
  private mfaEmail = '';

  get email() { return this.loginForm.controls.email; }
  get password() { return this.loginForm.controls.password; }
  get otpCode() { return this.otpForm.controls.otpCode; }

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
            this.mfaEmail = this.loginForm.getRawValue().email;
            this.otpForm.reset();
            this.step.set('OTP');
            return;
          }

          if (!res.token) {
            this.errorMessage = 'Login response is missing access token.';
            return;
          }

          this.authService.setToken(res.token);
          this.router.navigate(['/student-dashboard']);
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

  onVerifyOtp(): void {
    if (this.otpForm.invalid || !this.mfaEmail) {
      this.otpForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const payload = {
      email: this.mfaEmail,
      otpCode: this.otpForm.getRawValue().otpCode
    };

    this.http.post<VerifyOtpResponse>(`${environment.apiUrl}/auth/verify-mfa`, payload)
      .subscribe({
        next: (res) => {
          this.isLoading = false;
          const token = res.token ?? res.accessToken;

          if (!token) {
            this.errorMessage = 'OTP verification succeeded but no access token was returned.';
            return;
          }

          this.authService.setToken(token);
          this.router.navigate(['/student-dashboard']);
        },
        error: (err: HttpErrorResponse) => {
          this.isLoading = false;
          if (err.status === 401 || err.status === 400) {
            this.errorMessage = 'Invalid OTP code. Please try again.';
          } else if (err.status === 0) {
            this.errorMessage = 'Unable to connect to the server. Please check your connection.';
          } else {
            this.errorMessage = 'An unexpected error occurred while verifying OTP.';
          }
        }
      });
  }
}
