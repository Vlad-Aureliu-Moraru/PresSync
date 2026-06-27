import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationItem } from '../../models/notification';

@Component({
  selector: 'app-notification-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notification-card.component.html',
  styleUrl: './notification-card.component.scss',
})
export class NotificationCardComponent {
  @Input({ required: true }) notification!: NotificationItem;
  @Input() compact = false;
  @Output() markRead = new EventEmitter<number>();
  @Output() dismiss = new EventEmitter<number>();

  get iconPath(): string {
    switch (this.notification.type) {
      case 'attendance_reminder':
        return 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z';
      case 'category_update':
        return 'M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z';
      case 'success':
        return 'M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z';
      case 'error':
        return 'M12 2C6.47 2 2 6.47 2 12s4.47 10 10 10 10-4.47 10-10S17.53 2 12 2zm5 13.59L15.59 17 12 13.41 8.41 17 7 15.59 10.59 12 7 8.41 8.41 7 12 10.59 15.59 7 17 8.41 13.41 12 17 15.59z';
      case 'system':
        return 'M12 22c1.1 0 2-.9 2-2h-4c0 1.1.89 2 2 2zm6-6v-5c0-3.07-1.64-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.63 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2z';
    }
  }

  get relativeTime(): string {
    const diff = Date.now() - new Date(this.notification.timestamp).getTime();
    const seconds = Math.floor(diff / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);

    if (seconds < 60) return 'just now';
    if (minutes < 60) return `${minutes}m ago`;
    if (hours < 24) return `${hours}h ago`;
    if (days < 7) return `${days}d ago`;
    return new Date(this.notification.timestamp).toLocaleDateString();
  }

  onMarkRead(): void {
    if (!this.notification.read) {
      this.markRead.emit(this.notification.id);
    }
  }

  onDismiss(event: Event): void {
    event.stopPropagation();
    this.dismiss.emit(this.notification.id);
  }
}
