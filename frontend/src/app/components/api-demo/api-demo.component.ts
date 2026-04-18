import { Component } from '@angular/core';
import { ApiError } from '../../api/api-error';
import { AuthenticationRequest, UserGet } from '../../api/api.models';
import { AuthService } from '../../api/services/auth.service';
import { UserService } from '../../api/services/user.service';

@Component({
  selector: 'app-api-demo',
  templateUrl: './api-demo.component.html',
})
export class ApiDemoComponent {
  users: UserGet[] = [];
  loading = false;
  error: ApiError | null = null;
  token: string | null = null;

  email = '';
  password = '';

  constructor(
    private readonly authService: AuthService,
    private readonly userService: UserService,
  ) {}

  login(): void {
    this.loading = true;
    this.error = null;
    const payload: AuthenticationRequest = {
      email: this.email,
      password: this.password,
    };

    this.authService.login(payload).subscribe({
      next: (response) => {
        this.token = response.token;
        this.loadUsers();
      },
      error: (error: ApiError) => {
        this.error = error;
        this.loading = false;
      },
    });
  }

  loadUsers(): void {
    this.loading = true;
    this.error = null;

    this.userService.getAllUsers(this.token ?? undefined).subscribe({
      next: (users) => {
        this.users = users;
        this.loading = false;
      },
      error: (error: ApiError) => {
        this.error = error;
        this.loading = false;
      },
    });
  }
}
