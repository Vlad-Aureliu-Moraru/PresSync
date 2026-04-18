import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { API_BASE_URL } from '../api.config';
import { UserCreateRequest, UserGet } from '../api.models';
import { BaseApiService } from '../base-api.service';

@Injectable({
  providedIn: 'root',
})
export class UserService extends BaseApiService {
  constructor(
    private readonly http: HttpClient,
    @Inject(API_BASE_URL) baseUrl: string,
  ) {
    super(baseUrl);
  }

  getAllUsers(token?: string): Observable<UserGet[]> {
    return this.http
      .get<UserGet[]>(this.buildUrl('/user'), {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('user.getAll')));
  }

  getUserById(id: number, token?: string): Observable<UserGet> {
    return this.http
      .get<UserGet>(this.buildUrl(`/user/${id}`), {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('user.getById')));
  }

  updateUser(id: number, payload: UserCreateRequest, token?: string): Observable<void> {
    return this.http
      .put<void>(this.buildUrl(`/user/${id}`), payload, {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('user.update')));
  }

  deleteUser(id: number, token?: string): Observable<void> {
    return this.http
      .delete<void>(this.buildUrl(`/user/${id}`), {
        headers: this.authHeaders(token),
      })
      .pipe(catchError(this.handleError('user.delete')));
  }
}
