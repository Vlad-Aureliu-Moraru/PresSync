import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { API_BASE_URL } from '../api.config';
import { AuthenticationRequest, AuthenticationResponse, UserCreateRequest } from '../api.models';
import { BaseApiService } from '../base-api.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService extends BaseApiService {
  constructor(
    private readonly http: HttpClient,
    @Inject(API_BASE_URL) baseUrl: string,
  ) {
    super(baseUrl);
  }

  register(payload: UserCreateRequest): Observable<AuthenticationResponse> {
    return this.http
      .post<AuthenticationResponse>(this.buildUrl('/auth/register'), payload)
      .pipe(catchError(this.handleError('auth.register')));
  }

  login(payload: AuthenticationRequest): Observable<AuthenticationResponse> {
    return this.http
      .post<AuthenticationResponse>(this.buildUrl('/auth/login'), payload)
      .pipe(catchError(this.handleError('auth.login')));
  }
}
