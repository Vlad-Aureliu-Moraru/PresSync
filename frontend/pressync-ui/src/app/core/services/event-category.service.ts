import { Injectable, inject, signal } from '@angular/core';
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
  specificDate?: string;
  categoryConfig?: CategoryConfig;
  createdBy?: { id: number; email: string; name: string; surname: string } | null;
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

export interface UpdateEventCategoryRequest {
  name: string;
  startingTime: string;
  endTime: string;
  attendanceTimeStart: string;
  attendanceDuration: number;
  repeatable: boolean;
  configId?: number | null;
  repeatableType: string | null;
  repeatsOnSpecificDay: string | null;
  baseDate: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class EventCategoryService {
  private http = inject(HttpClient);

  private readonly scheduleRefreshSignal = signal(0);
  readonly scheduleRefreshTrigger = this.scheduleRefreshSignal.asReadonly();

  notifyScheduleChanged(): void {
    this.scheduleRefreshSignal.update((v) => v + 1);
  }

  getTodaySchedule(): Observable<EventCategory[]> {
    return this.http.get<EventCategory[]>(`${environment.apiUrl}/eventCategory/today`);
  }

  getAllCategories(): Observable<EventCategory[]> {
    return this.http.get<EventCategory[]>(`${environment.apiUrl}/eventCategory`);
  }

  getCategoryById(id: number | string): Observable<EventCategory> {
    return this.http.get<EventCategory>(`${environment.apiUrl}/eventCategory/${id}`);
  }

  createCategory(payload: CreateEventCategoryRequest): Observable<void> {
    return this.http.post<void>(`${environment.apiUrl}/eventCategory/create`, payload);
  }

  updateCategory(id: number, payload: UpdateEventCategoryRequest): Observable<void> {
    return this.http.put<void>(`${environment.apiUrl}/eventCategory/${id}`, payload);
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/eventCategory/${id}`);
  }
}
