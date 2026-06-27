export type NotificationType = 'attendance_reminder' | 'category_update' | 'success' | 'error' | 'system';

export interface NotificationItem {
  id: number;
  type: NotificationType;
  title: string;
  message?: string;
  timestamp: Date;
  read: boolean;
  actionUrl?: string;
  categoryId?: number;
}
