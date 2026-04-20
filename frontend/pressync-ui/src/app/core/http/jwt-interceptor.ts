import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../auth/auth';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  // Inject the AuthService
  const authService = inject(AuthService);
  const token = authService.getToken();

  // If we have a token, clone the request and add the Authorization header
  if (token) {
    const clonedRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(clonedRequest); // Send the cloned request to Spring Boot
  }

  // If no token (like when logging in), just send the normal request
  return next(req);
};
