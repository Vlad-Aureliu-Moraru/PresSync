import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { API_BASE_URL } from '../api.config';
import {
  AttendanceCreateRequest,
  AttendanceDetail,
  AttendanceGet,
  EventCategoryStats,
} from '../api.models';
import { BaseApiService } from '../base-api.service';

@Injectable({
  providedIn: 'root',
})
export class AttendanceService extends BaseApiService {
  constructor(
    private readonly http: HttpClient,
    @Inject(API_BASE_URL) baseUrl: string,
  ) {
    super(baseUrl);
  }

  getCategoryStats(categoryId: number, token?: string): Observable<EventCategoryStats> {
    return this.http
      .get<EventCategoryStats>(this.buildUrl(`/attendance/stats/category/${categoryId}`), {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('attendance.getCategoryStats')));
  }

  getAllAttendance(token?: string): Observable<AttendanceGet[]> {
    return this.http
      .get<AttendanceGet[]>(this.buildUrl('/attendance'), {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('attendance.getAll')));
  }

  getAttendanceById(id: number, token?: string): Observable<AttendanceDetail> {
    return this.http
      .get<AttendanceDetail>(this.buildUrl(`/attendance/${id}`), {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('attendance.getById')));
  }

  getAttendanceByUser(userId: number, token?: string): Observable<AttendanceDetail[]> {
    return this.http
      .get<AttendanceDetail[]>(this.buildUrl(`/attendance/user/${userId}`), {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('attendance.getByUser')));
  }

  markAttendance(token?: string): Observable<string> {
    return this.http
      .post<string>(this.buildUrl('/attendance/mark'), null, {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('attendance.mark')));
  }

  updateAttendance(id: number, payload: AttendanceCreateRequest, token?: string): Observable<string> {
    return this.http
      .put<string>(this.buildUrl(`/attendance/${id}`), payload, {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('attendance.update')));
  }
}
