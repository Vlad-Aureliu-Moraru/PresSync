import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly TOKEN_KEY = 'pressync_jwt';

  constructor() { }

  // Save the token after a successful login
  setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  // Get the token (used by the Interceptor)
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  // Clear the token on logout
  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  // Quick check if the user is logged in
  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
