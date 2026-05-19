import { Component, input, computed, signal, ElementRef, inject, AfterViewInit, OnDestroy, effect } from '@angular/core';

export type ChartPeriod = 'daily' | 'weekly' | 'monthly' | 'yearly';

export interface BarChartItem {
  label: string;
  value: number;
  color?: string;
  date?: string;
}

const PERIOD_OPTIONS: { value: ChartPeriod; label: string }[] = [
  { value: 'daily', label: 'Daily' },
  { value: 'weekly', label: 'Weekly' },
  { value: 'monthly', label: 'Monthly' },
  { value: 'yearly', label: 'Yearly' },
];

const MONTHS = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];

@Component({
  selector: 'app-bar-chart',
  standalone: true,
  imports: [],
  template: `
    <div class="bar-chart-root" #chartRoot>
      <div class="chart-header">
        <div class="period-selector" #periodSelector>
          <button class="period-btn" (click)="toggleDropdown()" (blur)="closeDropdown()">
            <span>{{ periodLabel() }}</span>
            <svg class="chevron" [class.open]="dropdownOpen()" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="6 9 12 15 18 9"></polyline>
            </svg>
          </button>
          @if (dropdownOpen()) {
            <div class="period-menu">
              @for (opt of periodOptions; track opt.value) {
                <button
                  class="period-option"
                  [class.active]="selectedPeriod() === opt.value"
                  (mousedown)="selectPeriod(opt.value); $event.preventDefault()"
                >{{ opt.label }}</button>
              }
            </div>
          }
        </div>
      </div>

      <div class="chart-wrapper" #wrapperEl>
        <svg
          [attr.viewBox]="viewBox()"
          class="chart-svg"
          preserveAspectRatio="xMidYMid meet"
          [attr.width]="wrapperWidth()"
        >
          @for (bar of bars(); track $index) {
            <g class="bar-group">
              <rect
                [attr.x]="bar.x"
                [attr.y]="bar.y"
                [attr.width]="bar.width"
                [attr.height]="bar.height"
                [attr.rx]="Math.min(bar.width / 2, 3)"
                [attr.fill]="bar.color"
                class="bar-rect"
              />
              @if (showValues() && bar.width >= 14) {
                <text
                  [attr.x]="bar.x + bar.width / 2"
                  [attr.y]="bar.y - 5"
                  class="bar-value"
                  text-anchor="middle"
                >{{ bar.value }}</text>
              }
              <text
                [attr.x]="bar.x + bar.width / 2"
                [attr.y]="chartHeight() + 12"
                class="bar-label"
                text-anchor="middle"
              >{{ bar.label }}</text>
            </g>
          }
        </svg>
      </div>
    </div>
  `,
  styles: [`
    .bar-chart-root {
      display: flex;
      flex-direction: column;
      gap: 0.75rem;
    }
    .chart-header {
      display: flex;
      justify-content: flex-end;
    }
    .period-selector {
      position: relative;
    }
    .period-btn {
      display: inline-flex;
      align-items: center;
      gap: 0.4rem;
      padding: 0.35rem 0.75rem;
      font-size: 0.8125rem;
      font-weight: 500;
      color: var(--ps-text);
      background: rgba(255, 255, 255, 0.05);
      border: 1px solid var(--ps-border);
      border-radius: 6px;
      cursor: pointer;
      transition: background var(--ps-transition);
    }
    .period-btn:hover {
      background: rgba(255, 255, 255, 0.08);
    }
    .period-btn .chevron {
      width: 14px;
      height: 14px;
      transition: transform 0.2s;
    }
    .period-btn .chevron.open {
      transform: rotate(180deg);
    }
    .period-menu {
      position: absolute;
      top: 100%;
      right: 0;
      margin-top: 4px;
      min-width: 110px;
      background: var(--secondary-bg);
      border: 1px solid var(--ps-border);
      border-radius: 8px;
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.5);
      z-index: 50;
      overflow: hidden;
    }
    .period-option {
      display: block;
      width: 100%;
      padding: 0.5rem 1rem;
      font-size: 0.8125rem;
      color: var(--ps-text);
      background: none;
      border: none;
      text-align: left;
      cursor: pointer;
      transition: background var(--ps-transition);
    }
    .period-option:hover {
      background: rgba(255, 255, 255, 0.06);
    }
    .period-option.active {
      color: var(--accent-action);
      font-weight: 600;
    }
    .chart-wrapper {
      width: 100%;
    }
    .chart-svg {
      display: block;
      width: 100%;
      height: auto;
    }
    .bar-rect {
      transition: height 0.4s ease, y 0.4s ease, fill 0.3s ease;
    }
    .bar-value {
      fill: var(--ps-text);
      font-size: 9px;
      font-weight: 600;
    }
    .bar-label {
      fill: var(--ps-text-muted);
      font-size: 7px;
    }
  `]
})
export class BarChartComponent implements AfterViewInit, OnDestroy {
  readonly data = input.required<BarChartItem[]>();
  readonly height = input(220);
  readonly showValues = input(true);

  protected readonly periodOptions = PERIOD_OPTIONS;
  protected readonly Math = Math;

  private hostEl = inject(ElementRef<HTMLElement>);
  private resizeObserver: ResizeObserver | null = null;

  protected readonly selectedPeriod = signal<ChartPeriod>('daily');
  protected readonly dropdownOpen = signal(false);
  protected readonly wrapperWidth = signal(600);

  protected readonly defaultColor = 'var(--accent-action)';
  protected readonly gap = 6;
  protected readonly padding = 14;

  ngAfterViewInit() {
    const wrapper = this.hostEl.nativeElement.querySelector('.chart-wrapper') as HTMLElement;
    if (wrapper) {
      this.resizeObserver = new ResizeObserver(entries => {
        for (const entry of entries) {
          this.wrapperWidth.set(entry.contentRect.width);
        }
      });
      this.resizeObserver.observe(wrapper);
    }
  }

  ngOnDestroy() {
    this.resizeObserver?.disconnect();
  }

  protected readonly periodLabel = computed(() =>
    PERIOD_OPTIONS.find(o => o.value === this.selectedPeriod())?.label ?? 'Daily'
  );

  protected readonly displayData = computed(() => {
    const raw = this.data();
    if (!raw.length) return [];

    const period = this.selectedPeriod();

    if (period === 'daily') {
      return raw;
    }

    const groups = new Map<string, { sum: number; color: string; dates: Date[] }>();

    for (const item of raw) {
      const date = item.date ? new Date(item.date + 'T00:00:00') : new Date();
      const key = this.getPeriodKey(date, period);
      const existing = groups.get(key) || { sum: 0, color: item.color || this.defaultColor, dates: [] };
      existing.sum += item.value;
      existing.dates.push(date);
      groups.set(key, existing);
    }

    const sorted = [...groups.entries()].sort(([a], [b]) => a.localeCompare(b));
    return sorted.map(([key, g]) => ({
      label: this.getPeriodLabel(g.dates[0], period, key),
      value: g.sum,
      color: g.color,
      date: undefined,
    }));
  });

  protected readonly chartHeight = computed(() => this.height() - 30);

  protected readonly maxValue = computed(() =>
    Math.max(...this.displayData().map(d => d.value), 1)
  );

  protected readonly barWidth = computed(() => {
    const count = this.displayData().length;
    if (count === 0) return 40;
    return Math.max(3, (this.wrapperWidth() - this.padding * 2 - this.gap * (count - 1)) / count);
  });

  protected readonly totalWidth = computed(() => {
    const count = this.displayData().length;
    if (count === 0) return this.wrapperWidth();
    return count * (this.barWidth() + this.gap) + this.padding * 2;
  });

  protected readonly viewBox = computed(() =>
    `0 0 ${this.totalWidth()} ${this.height()}`
  );

  protected readonly bars = computed(() => {
    const items = this.displayData();
    const max = this.maxValue();
    const chartH = this.chartHeight();
    const bw = this.barWidth();

    return items.map((item, i) => {
      const x = this.padding + i * (bw + this.gap);
      const barHeight = max > 0 ? (item.value / max) * chartH : 0;
      const y = chartH - barHeight;
      return {
        x,
        y,
        width: bw,
        height: barHeight,
        value: item.value,
        label: item.label,
        color: item.color || this.defaultColor,
        date: item.date,
      };
    });
  });

  protected toggleDropdown() {
    this.dropdownOpen.update(v => !v);
  }

  protected closeDropdown() {
    this.dropdownOpen.set(false);
  }

  protected selectPeriod(period: ChartPeriod) {
    this.selectedPeriod.set(period);
    this.dropdownOpen.set(false);
  }

  private getISOWeek(date: Date): number {
    const d = new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate()));
    const dayNum = d.getUTCDay() || 7;
    d.setUTCDate(d.getUTCDate() + 4 - dayNum);
    const yearStart = new Date(Date.UTC(d.getUTCFullYear(), 0, 1));
    return Math.ceil(((d.getTime() - yearStart.getTime()) / 86400000 + 1) / 7);
  }

  private getPeriodKey(date: Date, period: ChartPeriod): string {
    switch (period) {
      case 'daily':
        return date.toISOString().split('T')[0];
      case 'weekly':
        return `${date.getFullYear()}-W${String(this.getISOWeek(date)).padStart(2, '0')}`;
      case 'monthly':
        return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}`;
      case 'yearly':
        return String(date.getFullYear());
    }
  }

  private getPeriodLabel(date: Date, period: ChartPeriod, key: string): string {
    switch (period) {
      case 'daily':
        return `${MONTHS[date.getMonth()]} ${date.getDate()}`;
      case 'weekly': {
        const weekNum = this.getISOWeek(date);
        return `W${weekNum}`;
      }
      case 'monthly':
        return MONTHS[date.getMonth()];
      case 'yearly':
        return key;
    }
  }
}
