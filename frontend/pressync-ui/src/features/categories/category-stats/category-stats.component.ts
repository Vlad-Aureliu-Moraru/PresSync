import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AttendanceService, EventCategoryStatsDTO } from '../../../app/core/services/attendance.service';
import { EventCategoryService, EventCategory } from '../../../app/core/services/event-category.service';

@Component({
  selector: 'app-category-stats',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './category-stats.component.html',
  styleUrls: ['./category-stats.component.scss']
})
export class CategoryStatsComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private attendanceService = inject(AttendanceService);
  private eventCategoryService = inject(EventCategoryService);

  // States
  category = signal<EventCategory | null>(null);
  stats = signal<EventCategoryStatsDTO | null>(null);
  isLoading = signal<boolean>(true);
  errorMessage = signal<string>('');

  ngOnInit(): void {
    const categoryId = this.route.snapshot.paramMap.get('id');
    if (categoryId) {
      this.fetchData(categoryId);
    } else {
      this.errorMessage.set('Invalid category ID.');
      this.isLoading.set(false);
    }
  }

  private fetchData(id: string): void {
    this.isLoading.set(true);
    let categoryLoaded = false;
    let statsLoaded = false;

    // Check completion condition inside closure to manage loading state manually
    const checkCompletion = () => {
      if (categoryLoaded && statsLoaded) {
        this.isLoading.set(false);
      }
    };

    // Parallel-like fetching without complex RxJS forks to keep things simple as requested
    this.eventCategoryService.getCategoryById(id).subscribe({
      next: (cat) => {
        this.category.set(cat);
        categoryLoaded = true;
        checkCompletion();
      },
      error: () => {
        this.errorMessage.set('Failed to load category details.');
        this.isLoading.set(false);
      }
    });

    this.attendanceService.getCategoryStats(id).subscribe({
      next: (stat) => {
        this.stats.set(stat);
        statsLoaded = true;
        checkCompletion();
      },
      error: () => {
        this.errorMessage.set('Failed to load attendance stats.');
        this.isLoading.set(false);
      }
    });
  }
}
