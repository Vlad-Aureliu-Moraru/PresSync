import { Injectable, signal, effect } from '@angular/core';

export type Theme = 'dark' | 'light';

const STORAGE_KEY = 'pressync-theme';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  readonly theme = signal<Theme>('dark');

  constructor() {
    const stored = localStorage.getItem(STORAGE_KEY) as Theme | null;
    const preferred: Theme =
      stored ?? (window.matchMedia('(prefers-color-scheme: light)').matches ? 'light' : 'dark');

    this.theme.set(preferred);
    this.applyTheme(preferred);

    effect(() => {
      const t = this.theme();
      localStorage.setItem(STORAGE_KEY, t);
      this.applyTheme(t);
    });
  }

  toggle(): void {
    this.theme.update((t) => (t === 'dark' ? 'light' : 'dark'));
  }

  private applyTheme(t: Theme): void {
    document.documentElement.setAttribute('data-theme', t);
  }
}
