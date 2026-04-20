import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { EventCategoryService, EventCategory } from '../../../app/core/services/event-category.service';

@Component({
  selector: 'app-category-search',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './category-search.component.html',
  styleUrls: ['./category-search.component.scss']
})
export class CategorySearchComponent implements OnInit {
  private eventCategoryService = inject(EventCategoryService);
  private router = inject(Router);

  // States
  categories = signal<EventCategory[]>([]);
  searchQuery = signal<string>('');
  isLoading = signal<boolean>(true);
  errorMessage = signal<string>('');

  // Computed signal for filtered list
  filteredCategories = computed(() => {
    const query = this.searchQuery().toLowerCase().trim();
    if (!query) {
      return this.categories();
    }
    return this.categories().filter(category => 
      category.name.toLowerCase().includes(query)
    );
  });

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
      }
    });
  }

  onSearchChange(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.searchQuery.set(target.value);
  }

  goToStats(id: number): void {
    this.router.navigate(['/category-stats', id]);
  }
}
