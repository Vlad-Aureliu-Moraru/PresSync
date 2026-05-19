import { Component, inject, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { NotificationService } from '../../services/notification.service';
import { NotificationCardComponent } from '../notification-card/notification-card.component';

@Component({
  selector: 'app-notification-panel',
  standalone: true,
  imports: [CommonModule, RouterLink, NotificationCardComponent],
  templateUrl: './notification-panel.component.html',
  styleUrl: './notification-panel.component.scss',
})
export class NotificationPanelComponent {
  readonly notificationService = inject(NotificationService);

  @Input() isOpen = false;
  @Output() closePanel = new EventEmitter<void>();

  onBackdropClick(): void {
    this.closePanel.emit();
  }

  onPanelClick(event: Event): void {
    event.stopPropagation();
  }

  onMarkRead(id: string): void {
    this.notificationService.markAsRead(id);
  }

  onDismiss(id: string): void {
    this.notificationService.dismiss(id);
  }

  onMarkAllRead(): void {
    this.notificationService.markAllAsRead();
  }
}
