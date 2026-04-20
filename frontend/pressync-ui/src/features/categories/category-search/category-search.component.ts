import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { EventCategory, EventCategoryService } from '../../../app/core/services/event-category.service';

@Component({
  selector: 'app-category-search',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './category-search.component.html',
  styleUrl: './category-search.component.scss'
})
export class CategorySearchComponent implements OnInit {
  private eventCategoryService = inject(EventCategoryService);
  private router = inject(Router);

  categories = signal<EventCategory[]>([]);
  searchTerm = signal('');
  isLoading = signal(true);
  errorMessage = signal('');

  filteredCategories = computed(() => {
    const term = this.searchTerm().trim().toLowerCase();
    const all = this.categories();

    if (!term) {
      return all;
    }

    return all.filter(category => category.name.toLowerCase().includes(term));
  });

  ngOnInit(): void {
    this.eventCategoryService.getAllCategories().subscribe({
      next: (categories) => {
        this.categories.set(categories);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Unable to load categories right now.');
        this.isLoading.set(false);
      }
    });
  }

  onSearchTermChange(value: string): void {
    this.searchTerm.set(value);
  }

  openCategoryStats(categoryId: number): void {
    this.router.navigate(['/category-stats', categoryId]);
  }
}
