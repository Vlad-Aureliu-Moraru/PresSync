import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './core/layout/navbar/navbar.component';
import { TransientNotificationComponent } from './shared/components/transient-notification/transient-notification.component';
import { AttendanceService } from './core/services/attendance.service';
import { ThemeService } from './core/services/theme.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavbarComponent, TransientNotificationComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit, OnDestroy {
  private attendanceService = inject(AttendanceService);
  readonly themeService = inject(ThemeService);

  protected title = 'pressync-ui';

  ngOnInit(): void {
    this.attendanceService.startMonitoring();
  }

  ngOnDestroy(): void {
    this.attendanceService.stopMonitoring();
  }
}
