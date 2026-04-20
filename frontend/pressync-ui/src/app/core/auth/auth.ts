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

  // Decode the JWT payload safely
  private decodeJwtPayload(): any {
    const token = this.getToken();
    if (!token) return null;

    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));

      return JSON.parse(jsonPayload);
    } catch (e) {
      console.error('Failed to decode JWT:', e);
      return null;
    }
  }

  // Extract User ID
  getUserId(): number | null {
    const payload = this.decodeJwtPayload();
    return payload ? payload.userId : null;
  }

  // Extract Role
  getUserRole(): string | null {
    const payload = this.decodeJwtPayload();
    return payload ? payload.role : null;
  }

  // Check if role is Admin or Moderator
  isAdminOrModerator(): boolean {
    const role = this.getUserRole();
    return role === 'ADMIN' || role === 'MODERATOR';
  }
}
