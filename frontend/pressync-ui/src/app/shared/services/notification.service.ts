import { Injectable, signal, computed } from '@angular/core';
import { NotificationItem, NotificationType } from '../models/notification';
import { NotificationApiService } from './notification-api.service';

export interface FlashNotification {
  id: string;
  type: NotificationType;
  title: string;
  message?: string;
}

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private readonly notificationsSignal = signal<NotificationItem[]>([]);
  readonly notifications = this.notificationsSignal.asReadonly();

  readonly unreadCount = computed(() =>
    this.notifications().filter((n) => !n.read).length
  );

  private readonly flashSignal = signal<FlashNotification | null>(null);
  readonly flashNotification = this.flashSignal.asReadonly();

  private flashTimeout: ReturnType<typeof setTimeout> | null = null;

  constructor(private readonly notificationApi: NotificationApiService) {
    this.loadNotifications();
  }

  loadNotifications(): void {
    this.notificationApi.getAll().subscribe({
      next: (items) => {
        this.notificationsSignal.set(
          items.map((item) => ({
            ...item,
            timestamp: new Date(item.timestamp),
          }))
        );
      },
      error: () => {
        this.notificationsSignal.set([]);
      },
    });
  }

  showFlash(type: FlashNotification['type'], title: string, message?: string, durationMs = 4500): void {
    if (this.flashTimeout) {
      clearTimeout(this.flashTimeout);
    }
    const id = `${Date.now()}-${Math.random().toString(16).slice(2)}`;
    this.flashSignal.set({ id, type, title, message });
    this.flashTimeout = setTimeout(() => {
      this.flashSignal.set(null);
      this.flashTimeout = null;
    }, durationMs);
  }

  dismissFlash(): void {
    if (this.flashTimeout) {
      clearTimeout(this.flashTimeout);
      this.flashTimeout = null;
    }
    this.flashSignal.set(null);
  }

  markAsRead(id: number): void {
    this.notificationApi.markAsRead(id).subscribe({
      next: () => {
        this.notificationsSignal.update((items) =>
          items.map((n) => (n.id === id ? { ...n, read: true } : n))
        );
      },
    });
  }

  markAllAsRead(): void {
    this.notificationApi.markAllAsRead().subscribe({
      next: () => {
        this.notificationsSignal.update((items) =>
          items.map((n) => ({ ...n, read: true }))
        );
      },
    });
  }

  add(notification: Omit<NotificationItem, 'id' | 'timestamp'>): void {
    this.notificationApi.create(notification).subscribe({
      next: (created) => {
        const item: NotificationItem = {
          ...created,
          timestamp: new Date(created.timestamp),
        };
        this.notificationsSignal.update((items) => [item, ...items]);
      },
    });
  }

  dismiss(id: number): void {
    this.notificationApi.dismiss(id).subscribe({
      next: () => {
        this.notificationsSignal.update((items) =>
          items.filter((n) => n.id !== id)
        );
      },
    });
  }
}
