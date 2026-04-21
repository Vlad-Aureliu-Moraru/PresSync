import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AttendanceService, AttendanceRecord } from '../../../app/core/services/attendance.service';

@Component({
  selector: 'app-user-attendance',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './user-attendance.component.html',
  styleUrls: ['./user-attendance.component.scss'],
  providers: [DatePipe]
})
export class UserAttendanceComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private attendanceService = inject(AttendanceService);

  records = signal<AttendanceRecord[]>([]);
  isLoading = signal<boolean>(true);
  errorMessage = signal<string>('');
  currentUserId = signal<string>('');

  ngOnInit(): void {
    const userId = this.route.snapshot.paramMap.get('id');
    if (userId) {
      this.currentUserId.set(userId);
      this.fetchAttendance(userId);
    } else {
      this.errorMessage.set('Invalid User ID provided.');
      this.isLoading.set(false);
    }
  }

  private fetchAttendance(userId: string): void {
    this.isLoading.set(true);
    this.attendanceService.getUserAttendance(userId).subscribe({
      next: (history) => {
        this.records.set(history);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Failed to load user attendance history.');
        this.isLoading.set(false);
      }
    });
  }
}
