import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface AttendanceRecord {
  id?: number;
  date: string;
  eventName: string;
  status: string;
}

export interface EventCategoryStatsDTO {
  averageAttendance: number;
  maxAttendance: number;
  attendanceRate: number;
}

@Injectable({
  providedIn: 'root'
})
export class AttendanceService {
  private http = inject(HttpClient);

  markAttendance(): Observable<any> {
    return this.http.post(`${environment.apiUrl}/attendance/mark`, {});
  }

  getUserAttendance(userId: string | number): Observable<AttendanceRecord[]> {
    return this.http.get<AttendanceRecord[]>(`${environment.apiUrl}/attendance/user/${userId}`);
  }

  getCategoryStats(categoryId: number | string): Observable<EventCategoryStatsDTO> {
    return this.http.get<EventCategoryStatsDTO>(`${environment.apiUrl}/attendance/stats/category/${categoryId}`);
  }
}
