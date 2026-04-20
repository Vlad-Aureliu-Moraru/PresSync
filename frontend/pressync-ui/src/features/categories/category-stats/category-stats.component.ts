import { Component, DestroyRef, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { AttendanceService, EventCategoryStatsDTO } from '../../../app/core/services/attendance.service';
import { EventCategory, EventCategoryService } from '../../../app/core/services/event-category.service';

@Component({
  selector: 'app-category-stats',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './category-stats.component.html',
  styleUrl: './category-stats.component.scss'
})
export class CategoryStatsComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private eventCategoryService = inject(EventCategoryService);
  private attendanceService = inject(AttendanceService);
  private destroyRef = inject(DestroyRef);

  category = signal<EventCategory | null>(null);
  stats = signal<EventCategoryStatsDTO | null>(null);
  isLoading = signal(true);
  errorMessage = signal('');

  ngOnInit(): void {
    this.route.paramMap
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((params) => {
        const categoryId = Number(params.get('id'));

        if (!Number.isFinite(categoryId) || categoryId <= 0) {
          this.errorMessage.set('Invalid category identifier.');
          this.category.set(null);
          this.stats.set(null);
          this.isLoading.set(false);
          return;
        }

        this.loadCategoryStats(categoryId);
      });
  }

  private loadCategoryStats(categoryId: number): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    forkJoin({
      categories: this.eventCategoryService.getAllCategories(),
      stats: this.attendanceService.getCategoryStats(categoryId)
    }).subscribe({
      next: ({ categories, stats }) => {
        const selectedCategory = categories.find((category) => category.id === categoryId) ?? null;

        if (!selectedCategory) {
          this.errorMessage.set('Category not found.');
          this.category.set(null);
          this.stats.set(null);
          this.isLoading.set(false);
          return;
        }

        this.category.set(selectedCategory);
        this.stats.set(stats);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Unable to load category stats right now.');
        this.category.set(null);
        this.stats.set(null);
        this.isLoading.set(false);
      }
    });
  }
}
