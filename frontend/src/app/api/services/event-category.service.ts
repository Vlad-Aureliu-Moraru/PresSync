import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { API_BASE_URL } from '../api.config';
import {
  CreateEventCategoryRequest,
  EventCategory,
  UpdateEventCategoryRequest,
} from '../api.models';
import { BaseApiService } from '../base-api.service';

@Injectable({
  providedIn: 'root',
})
export class EventCategoryService extends BaseApiService {
  constructor(
    private readonly http: HttpClient,
    @Inject(API_BASE_URL) baseUrl: string,
  ) {
    super(baseUrl);
  }

  getAllCategories(token?: string): Observable<EventCategory[]> {
    return this.http
      .get<EventCategory[]>(this.buildUrl('/eventCategory'), {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('eventCategory.getAll')));
  }

  getCategoryById(id: number, token?: string): Observable<EventCategory> {
    return this.http
      .get<EventCategory>(this.buildUrl(`/eventCategory/${id}`), {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('eventCategory.getById')));
  }

  createCategory(payload: CreateEventCategoryRequest, token?: string): Observable<string> {
    return this.http
      .post<string>(this.buildUrl('/eventCategory/create'), payload, {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('eventCategory.create')));
  }

  updateCategory(
    id: number,
    payload: UpdateEventCategoryRequest,
    token?: string,
  ): Observable<void> {
    return this.http
      .put<void>(this.buildUrl(`/eventCategory/${id}`), payload, {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('eventCategory.update')));
  }

  deleteCategory(id: number, token?: string): Observable<void> {
    return this.http
      .delete<void>(this.buildUrl(`/eventCategory/${id}`), {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('eventCategory.delete')));
  }
}
