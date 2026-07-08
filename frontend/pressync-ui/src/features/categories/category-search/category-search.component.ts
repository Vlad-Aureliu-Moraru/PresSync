import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { EventCategoryService, EventCategory } from '../../../app/core/services/event-category.service';
import { AuthService } from '../../../app/core/auth/auth';
import { NotificationService } from '../../../app/shared/services/notification.service';
import { CategoryCreateComponent } from '../../admin/category-create/category-create.component';
import { CategoryEditComponent } from '../../admin/category-edit/category-edit.component';
import { ConfirmDialogComponent } from '../../../app/shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-category-search',
  standalone: true,
  imports: [CommonModule, CategoryCreateComponent, CategoryEditComponent, ConfirmDialogComponent],
  templateUrl: './category-search.component.html',
  styleUrls: ['./category-search.component.scss'],
})
export class CategorySearchComponent implements OnInit {
  private eventCategoryService = inject(EventCategoryService);
  private router = inject(Router);
  readonly authService = inject(AuthService);
  private notificationService = inject(NotificationService);

  categories = signal<EventCategory[]>([]);
  searchQuery = signal<string>('');
  isLoading = signal<boolean>(true);
  errorMessage = signal<string>('');
  currentUserId = signal<number | null>(this.authService.getUserId());

  filteredCategories = computed(() => {
    const query = this.searchQuery().toLowerCase().trim();
    if (!query) {
      return this.categories();
    }
    return this.categories().filter((category) =>
      category.name.toLowerCase().includes(query),
    );
  });

  editingCategory = signal<EventCategory | null>(null);
  deletingCategory = signal<EventCategory | null>(null);
  creatingCategory = signal(false);
  isDeleting = signal(false);

  get deleteMessage(): string {
    const cat = this.deletingCategory();
    return cat ? `Are you sure you want to delete "${cat.name}"? This action cannot be undone.` : '';
  }

  canEditOrDelete(category: EventCategory): boolean {
    if (this.authService.isAdmin()) return true;
    if (category.createdBy && category.createdBy.id === this.currentUserId()) return true;
    return false;
  }

  ngOnInit(): void {
    this.fetchCategories();
  }

  private fetchCategories(): void {
    this.isLoading.set(true);
    this.eventCategoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categories.set(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Failed to load categories. Please try again.');
        this.isLoading.set(false);
      },
    });
  }

  onSearchChange(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.searchQuery.set(target.value);
  }

  goToStats(id: number): void {
    this.router.navigate(['/category-stats', id]);
  }

  openCreateModal(): void {
    this.creatingCategory.set(true);
  }

  closeCreateModal(): void {
    this.creatingCategory.set(false);
  }

  onCategoryCreated(): void {
    this.closeCreateModal();
    this.fetchCategories();
  }

  openEditModal(category: EventCategory): void {
    this.editingCategory.set(category);
  }

  closeEditModal(): void {
    this.editingCategory.set(null);
  }

  onCategoryUpdated(): void {
    this.closeEditModal();
    this.fetchCategories();
  }

  openDeleteDialog(category: EventCategory): void {
    this.deletingCategory.set(category);
  }

  closeDeleteDialog(): void {
    this.deletingCategory.set(null);
  }

  repeatTypeLabel(type: string): string {
    const map: Record<string, string> = {
      DAILY: 'Every Day',
      WEEKLY: 'Every Week',
      BIWEEKLY: 'Every 2 Weeks',
      MONTHLY: 'Every Month',
      YEARLY: 'Every Year',
    };
    return map[type] ?? type;
  }

  onDeleteConfirmed(): void {
    const category = this.deletingCategory();
    if (!category) return;

    this.isDeleting.set(true);
    this.eventCategoryService.deleteCategory(category.id).subscribe({
      next: () => {
        this.notificationService.showFlash(
          'success',
          'Category deleted',
          `"${category.name}" has been deleted.`,
        );
        this.eventCategoryService.notifyScheduleChanged();
        this.isDeleting.set(false);
        this.closeDeleteDialog();
        this.fetchCategories();
      },
      error: () => {
        this.notificationService.showFlash(
          'error',
          'Delete failed',
          'An error occurred while deleting the category.',
        );
        this.isDeleting.set(false);
        this.closeDeleteDialog();
      },
    });
  }
}
