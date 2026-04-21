import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface CategoryConfig {
  repeatableType: string;
  repeatsOnSpecificDay: string;
  baseDate: string;
}

export interface EventCategory {
  id: number;
  name: string;
  startingTime: string;
  endTime: string;
  attendanceTimeStart: string;
  attendanceDuration: number;
  repeatable: boolean;
  categoryConfig?: CategoryConfig;
}

export interface CreateEventCategoryRequest {
  name: string;
  startingTime: string;
  endTime: string;
  attendanceTimeStart: string;
  attendanceDuration: number;
  repeatable: boolean;
  repeatableType: string | null;
  repeatsOnSpecificDay: string | null;
  baseDate: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class EventCategoryService {
  private http = inject(HttpClient);

  getTodaySchedule(): Observable<EventCategory[]> {
    return this.http.get<EventCategory[]>(`${environment.apiUrl}/eventCategory/today`);
  }

  getAllCategories(): Observable<EventCategory[]> {
    return this.http.get<EventCategory[]>(`${environment.apiUrl}/eventCategory`);
  }

  getCategoryById(id: number | string): Observable<EventCategory> {
    return this.http.get<EventCategory>(`${environment.apiUrl}/eventCategory/${id}`);
  }

  createCategory(payload: CreateEventCategoryRequest): Observable<EventCategory> {
    return this.http.post<EventCategory>(`${environment.apiUrl}/eventCategory`, payload);
  }
}
