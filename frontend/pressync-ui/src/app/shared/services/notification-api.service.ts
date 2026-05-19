import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { NotificationItem } from '../models/notification';

@Injectable({
  providedIn: 'root',
})
export class NotificationApiService {
  private readonly apiUrl = `${environment.apiUrl}/notifications`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<NotificationItem[]> {
    return this.http.get<NotificationItem[]>(this.apiUrl);
  }

  getUnread(): Observable<NotificationItem[]> {
    return this.http.get<NotificationItem[]>(`${this.apiUrl}/unread`);
  }

  markAsRead(id: string): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/read`, {});
  }

  markAllAsRead(): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/read-all`, {});
  }

  dismiss(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
