import { Injectable, signal } from '@angular/core';

export type ToastType = 'success' | 'error' | 'info';

export interface ToastMessage {
  id: string;
  type: ToastType;
  title: string;
  message?: string;
  durationMs?: number;
}

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  private readonly toastsSignal = signal<ToastMessage[]>([]);
  readonly toasts = this.toastsSignal.asReadonly();

  show(message: Omit<ToastMessage, 'id'>): void {
    const id = `${Date.now()}-${Math.random().toString(16).slice(2)}`;
    const toast: ToastMessage = { id, ...message };
    this.toastsSignal.update((items) => [...items, toast]);

    const duration = message.durationMs ?? 4000;
    window.setTimeout(() => this.dismiss(id), duration);
  }

  dismiss(id: string): void {
    this.toastsSignal.update((items) => items.filter((item) => item.id !== id));
  }
}
