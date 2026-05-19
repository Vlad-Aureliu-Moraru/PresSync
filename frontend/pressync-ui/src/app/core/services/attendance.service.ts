import { Injectable, inject, signal, effect } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthService } from '../auth/auth';
import { EventCategoryService, EventCategory } from './event-category.service';

export interface AttendanceRecord {
  id: number;
  joinedAt: string;
  event: {
    date: string;
    eventCategory: {
      name: string;
      startingTime: string;
      endTime: string;
    };
  };
}

export interface EventAttendanceSummary {
  date: string;
  count: number;
}

export interface EventCategoryStatsDTO {
  averageAttendance: number;
  maxAttendance: number;
  attendanceRate: number;
  eventAttendanceSummaries: EventAttendanceSummary[];
}

export type MarkState = 'idle' | 'loading' | 'marked';

@Injectable({
  providedIn: 'root'
})
export class AttendanceService {
  private http = inject(HttpClient);
  private auth = inject(AuthService);
  private eventCategoryService = inject(EventCategoryService);

  readonly markState = signal<MarkState>('idle');

  private timers: ReturnType<typeof setTimeout>[] = [];
  private scheduleRefreshTimer: ReturnType<typeof setTimeout> | null = null;
  private monitoringStarted = false;

  constructor() {
    effect(() => {
      this.eventCategoryService.scheduleRefreshTrigger();
      if (this.monitoringStarted && this.auth.isAuthenticated()) {
        this.scheduleFromTodaySchedule();
      }
    });
  }

  markAttendance(): Observable<any> {
    return this.http.post(`${environment.apiUrl}/attendance/mark`, {});
  }

  getUserAttendance(userId: string | number): Observable<AttendanceRecord[]> {
    return this.http.get<AttendanceRecord[]>(`${environment.apiUrl}/attendance/user/${userId}`);
  }

  getCategoryStats(categoryId: number | string): Observable<EventCategoryStatsDTO> {
    return this.http.get<EventCategoryStatsDTO>(`${environment.apiUrl}/attendance/stats/category/${categoryId}`);
  }

  getUserCategoryAttendance(userId: number | string, categoryId: number | string): Observable<AttendanceRecord[]> {
    return this.http.get<AttendanceRecord[]>(`${environment.apiUrl}/attendance/user/${userId}/category/${categoryId}`);
  }

  startMonitoring(): void {
    if (!this.auth.isAuthenticated()) return;
    if (this.monitoringStarted) return;

    this.monitoringStarted = true;
    this.scheduleFromTodaySchedule();
    this.scheduleNextRefresh();
  }

  stopMonitoring(): void {
    this.monitoringStarted = false;
    this.clearTimers();
    if (this.scheduleRefreshTimer) {
      clearTimeout(this.scheduleRefreshTimer);
      this.scheduleRefreshTimer = null;
    }
  }

  private scheduleNextRefresh(): void {
    const jitter = Math.random() * 60000;
    this.scheduleRefreshTimer = setTimeout(() => {
      this.scheduleFromTodaySchedule();
      this.scheduleNextRefresh();
    }, 300000 + jitter);
  }

  private scheduleFromTodaySchedule(): void {
    this.clearTimers();
    this.eventCategoryService.getTodaySchedule().subscribe({
      next: (schedule) => this.scheduleWindows(schedule),
      error: () => {}
    });
  }

  private scheduleWindows(schedule: EventCategory[]): void {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const today = `${year}-${month}-${day}`;

    for (const event of schedule) {
      const parsed = event.attendanceTimeStart.split(':').map(Number);
      const openDate = new Date(`${today}T${String(parsed[0]).padStart(2, '0')}:${String(parsed[1] || 0).padStart(2, '0')}:00`);
      const closeDate = new Date(openDate.getTime() + (event.attendanceDuration || 0) * 60000);

      if (now >= openDate && now <= closeDate) {
        this.attemptMark();
      } else if (now < openDate) {
        const delay = openDate.getTime() - now.getTime() + Math.random() * 5000;
        const timer = setTimeout(() => this.attemptMark(), delay);
        this.timers.push(timer);
      }
    }
  }

  private attemptMark(): void {
    if (!this.auth.isAuthenticated()) return;
    if (this.markState() === 'marked') return;

    this.markState.set('loading');
    this.markAttendance().subscribe({
      next: () => this.markState.set('marked'),
      error: () => this.markState.set('idle')
    });
  }

  private clearTimers(): void {
    for (const t of this.timers) clearTimeout(t);
    this.timers = [];
  }
}
