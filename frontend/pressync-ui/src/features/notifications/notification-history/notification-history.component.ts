import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../../../app/shared/services/notification.service';
import { NotificationCardComponent } from '../../../app/shared/components/notification-card/notification-card.component';

@Component({
  selector: 'app-notification-history',
  standalone: true,
  imports: [CommonModule, NotificationCardComponent],
  templateUrl: './notification-history.component.html',
  styleUrl: './notification-history.component.scss',
})
export class NotificationHistoryComponent {
  readonly notificationService = inject(NotificationService);

  onMarkRead(id: number): void {
    this.notificationService.markAsRead(id);
  }

  onDismiss(id: number): void {
    this.notificationService.dismiss(id);
  }
}
