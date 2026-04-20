import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../auth/auth';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAdminOrModerator()) {
    return true;
  }

  // Not an admin or moderator, redirect to student dashboard
  return router.parseUrl('/student-dashboard');
};
