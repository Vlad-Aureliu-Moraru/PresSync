import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../app/core/auth/auth';
import { AttendanceService } from '../../../app/core/services/attendance.service';
import { environment } from '../../../environments/environment';

interface VerifyOtpResponse {
  token?: string;
  requiresMfa?: boolean;
  otpDestination?: string;
}

@Component({
  selector: 'app-mfa-validate',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './mfa-validate.html',
  styleUrls: ['./mfa-validate.scss']
})
export class MfaValidateComponent implements OnInit {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);
  private authService = inject(AuthService);
  private attendanceService = inject(AttendanceService);

  otpForm = this.fb.nonNullable.group({
    otpCode: ['', [Validators.required]]
  });

  errorMessage = '';
  isLoading = false;

  get otpCode() { return this.otpForm.controls.otpCode; }

  ngOnInit(): void {
    const mfaData = sessionStorage.getItem('pressync_mfa_data');
    if (!mfaData) {
      this.router.navigate(['/login']);
    }
  }

  onVerifyOtp(): void {
    if (this.otpForm.invalid) {
      this.otpForm.markAllAsTouched();
      return;
    }

    const mfaData = sessionStorage.getItem('pressync_mfa_data');
    if (!mfaData) {
      this.errorMessage = 'Session expired. Please log in again.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const payload = {
      email: JSON.parse(mfaData).email,
      otpCode: this.otpForm.getRawValue().otpCode
    };

    this.http.post<VerifyOtpResponse>(`${environment.apiUrl}/auth/verify-otp`, payload)
      .subscribe({
        next: (res) => {
          this.isLoading = false;

          if (!res.token) {
            this.errorMessage = 'OTP verification succeeded but no access token was returned.';
            return;
          }

          this.authService.setToken(res.token);
          sessionStorage.removeItem('pressync_mfa_data');
          this.attendanceService.stopMonitoring();
          this.attendanceService.startMonitoring();

          const role = this.authService.getUserRole();
          if (role === 'ADMIN' || role === 'MODERATOR') {
            this.router.navigate(['/superior-dashboard']);
          } else {
            this.router.navigate(['/user-dashboard']);
          }
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
