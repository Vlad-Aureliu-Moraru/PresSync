import { Injectable, signal, computed } from '@angular/core';
import { NotificationItem, NotificationType } from '../models/notification';

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

  constructor() {
    this.loadMockData();
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

  markAsRead(id: string): void {
    this.notificationsSignal.update((items) =>
      items.map((n) => (n.id === id ? { ...n, read: true } : n))
    );
  }

  markAllAsRead(): void {
    this.notificationsSignal.update((items) =>
      items.map((n) => ({ ...n, read: true }))
    );
  }

  add(notification: Omit<NotificationItem, 'id' | 'timestamp'>): void {
    const item: NotificationItem = {
      ...notification,
      id: `${Date.now()}-${Math.random().toString(16).slice(2)}`,
      timestamp: new Date(),
    };
    this.notificationsSignal.update((items) => [item, ...items]);
  }

  dismiss(id: string): void {
    this.notificationsSignal.update((items) =>
      items.filter((n) => n.id !== id)
    );
  }

  private loadMockData(): void {
    const now = Date.now();
    const mock: NotificationItem[] = [
      {
        id: '1',
        type: 'attendance_reminder',
        title: 'Attendance marked',
        message: 'You have been marked present for today\'s session.',
        timestamp: new Date(now - 1000 * 60 * 5),
        read: false,
      },
      {
        id: '2',
        type: 'category_update',
        title: 'New category available',
        message: 'A new event category "Morning Yoga" has been created.',
        timestamp: new Date(now - 1000 * 60 * 30),
        read: false,
        actionUrl: '/search',
      },
      {
        id: '3',
        type: 'success',
        title: 'Category created',
        message: 'Your category "Weekly Standup" was created successfully.',
        timestamp: new Date(now - 1000 * 60 * 60 * 2),
        read: false,
      },
      {
        id: '4',
        type: 'system',
        title: 'Welcome to Pressync',
        message: 'Your account has been created. Start by exploring available categories.',
        timestamp: new Date(now - 1000 * 60 * 60 * 24),
        read: true,
        actionUrl: '/search',
      },
      {
        id: '5',
        type: 'attendance_reminder',
        title: 'Missed attendance',
        message: 'You missed the morning session. Please check in with your supervisor.',
        timestamp: new Date(now - 1000 * 60 * 60 * 48),
        read: true,
      },
    ];
    this.notificationsSignal.set(mock);
  }
}
