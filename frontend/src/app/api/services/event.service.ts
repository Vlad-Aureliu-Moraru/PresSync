import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { API_BASE_URL } from '../api.config';
import { Event } from '../api.models';
import { BaseApiService } from '../base-api.service';

@Injectable({
  providedIn: 'root',
})
export class EventService extends BaseApiService {
  constructor(
    private readonly http: HttpClient,
    @Inject(API_BASE_URL) baseUrl: string,
  ) {
    super(baseUrl);
  }

  getAllEvents(token?: string): Observable<Event[]> {
    return this.http
      .get<Event[]>(this.buildUrl('/event'), {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('event.getAll')));
  }

  getEventById(id: number, token?: string): Observable<Event> {
    return this.http
      .get<Event>(this.buildUrl(`/event/${id}`), {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('event.getById')));
  }

  createEvent(payload: Event, token?: string): Observable<string> {
    return this.http
      .post<string>(this.buildUrl('/event'), payload, {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('event.create')));
  }

  updateEvent(id: number, payload: Event, token?: string): Observable<void> {
    return this.http
      .put<void>(this.buildUrl(`/event/${id}`), payload, {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('event.update')));
  }

  deleteEvent(id: number, token?: string): Observable<void> {
    return this.http
      .delete<void>(this.buildUrl(`/event/${id}`), {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('event.delete')));
  }
}
