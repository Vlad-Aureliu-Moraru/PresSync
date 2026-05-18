import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './core/layout/navbar/navbar.component';
import { TransientNotificationComponent } from './shared/components/transient-notification/transient-notification.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavbarComponent, TransientNotificationComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected title = 'pressync-ui';
}
