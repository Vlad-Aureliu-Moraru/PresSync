import { HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { ApiError } from './api-error';
import { API_BASE_URL } from './api.config';

@Injectable({
  providedIn: 'root',
})
export class BaseApiService {
  constructor(@Inject(API_BASE_URL) protected readonly baseUrl: string) {}

  protected buildUrl(path: string): string {
    if (!path.startsWith('/')) {
      return `${this.baseUrl}/${path}`;
    }
    return `${this.baseUrl}${path}`;
  }

  protected authHeaders(token?: string): HttpHeaders {
    if (!token) {
      return new HttpHeaders();
    }
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  protected handleError(operation: string) {
    return (error: HttpErrorResponse): Observable<never> => {
      const apiError: ApiError = {
        status: error.status,
        message: error.error?.message ?? error.message ?? 'Unexpected error',
        details: error.error,
        operation,
      };
      return throwError(() => apiError);
    };
  }
}
