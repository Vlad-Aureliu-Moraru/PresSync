import { Component, OnInit, inject, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AttendanceService, EventCategoryStatsDTO, AttendanceRecord } from '../../../app/core/services/attendance.service';
import { EventCategoryService, EventCategory } from '../../../app/core/services/event-category.service';
import { AuthService } from '../../../app/core/auth/auth';
import { BarChartComponent, BarChartItem } from '../../../app/shared/components/bar-chart/bar-chart.component';

@Component({
  selector: 'app-category-stats',
  standalone: true,
  imports: [CommonModule, RouterLink, BarChartComponent],
  templateUrl: './category-stats.component.html',
  styleUrls: ['./category-stats.component.scss']
})
export class CategoryStatsComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private attendanceService = inject(AttendanceService);
  private eventCategoryService = inject(EventCategoryService);
  private authService = inject(AuthService);

  category = signal<EventCategory | null>(null);
  stats = signal<EventCategoryStatsDTO | null>(null);
  userAttendance = signal<AttendanceRecord[]>([]);
  isLoading = signal(true);
  errorMessage = signal('');

  isAdminOrMod = computed(() => this.authService.isAdminOrModerator());
  userId = computed(() => this.authService.getUserId());

  chartData = computed<BarChartItem[]>(() => {
    const s = this.stats();
    if (!s?.eventAttendanceSummaries?.length) return [];

    if (this.isAdminOrMod()) {
      return s.eventAttendanceSummaries.map(item => ({
        label: this.formatDate(item.date),
        value: item.count,
        date: item.date,
      }));
    }

    const attendedDates = new Set(
      this.userAttendance().map(a => a.event.date)
    );
    return s.eventAttendanceSummaries.map(item => ({
      label: this.formatDate(item.date),
      value: item.count,
      color: attendedDates.has(item.date) ? '#22c55e' : '#3a3a3a',
      date: item.date,
    }));
  });

  personalStats = computed(() => {
    if (this.isAdminOrMod()) return null;
    const s = this.stats();
    const total = s?.eventAttendanceSummaries?.length ?? 0;
    const attended = this.userAttendance().length;
    const pct = total > 0 ? Math.round(attended / total * 100) : 0;
    return { attended, total, percentage: pct };
  });

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
    let loaded = 0;
    const total = this.isAdminOrMod() ? 2 : 3;

    const checkCompletion = () => {
      loaded++;
      if (loaded >= total) {
        this.isLoading.set(false);
      }
    };

    this.eventCategoryService.getCategoryById(id).subscribe({
      next: (cat) => {
        this.category.set(cat);
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
        checkCompletion();
      },
      error: () => {
        this.errorMessage.set('Failed to load attendance stats.');
        this.isLoading.set(false);
      }
    });

    if (!this.isAdminOrMod()) {
      const uid = this.userId();
      if (uid !== null) {
        this.attendanceService.getUserCategoryAttendance(uid, id).subscribe({
          next: (records) => {
            this.userAttendance.set(records);
            checkCompletion();
          },
          error: () => {
            // Non-critical — user can still see chart without personal data highlighted
            checkCompletion();
          }
        });
      } else {
        checkCompletion();
      }
    }
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

  private formatDate(dateStr: string): string {
    const date = new Date(dateStr);
    const months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
    return `${months[date.getMonth()]} ${date.getDate()}`;
  }
}
