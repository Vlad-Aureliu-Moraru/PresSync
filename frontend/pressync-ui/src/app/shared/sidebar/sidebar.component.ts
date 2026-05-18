import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';

type SidebarLink = {
  label: string;
  path: string;
  icon: 'dashboard' | 'search' | 'settings';
};

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.component.html'
})
export class SidebarComponent {
  readonly links: SidebarLink[] = [
    { label: 'Dashboard', path: '/dashboard', icon: 'dashboard' },
    { label: 'Search', path: '/search', icon: 'search' },
    { label: 'Settings', path: '/settings', icon: 'settings' }
  ];

  isCollapsed = signal(false);
  isMobileOpen = signal(false);

  toggleCollapse(): void {
    this.isCollapsed.update((state) => !state);
  }

  toggleMobile(): void {
    this.isMobileOpen.update((state) => !state);
  }
}
