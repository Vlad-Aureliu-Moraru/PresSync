import { Component, EventEmitter, Output, effect, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { toSignal } from '@angular/core/rxjs-interop';
import { startWith } from 'rxjs';
import { EventCategoryService, CreateEventCategoryRequest } from '../../../app/core/services/event-category.service';
import { NotificationService } from '../../../app/shared/services/notification.service';
import { TimeInputComponent } from '../../../app/shared/components/time-input/time-input.component';

@Component({
  selector: 'app-category-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TimeInputComponent],
  templateUrl: './category-create.component.html',
  styleUrl: './category-create.component.scss'
})
export class CategoryCreateComponent {
  private fb = inject(FormBuilder);
  private eventCategoryService = inject(EventCategoryService);
  private notificationService = inject(NotificationService);

  @Output() categoryCreated = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  categoryForm: FormGroup = this.fb.group({
    name: ['', [Validators.required]],
    startingTime: ['', [Validators.required]],
    endTime: ['', [Validators.required]],
    attendanceTimeStart: ['', [Validators.required]],
    attendanceDuration: [5, [Validators.required, Validators.min(5)]],
    repeatable: [false],
    repeatableType: [''],
    repeatsOnSpecificDay: [''],
    baseDate: ['']
  });

  readonly isRepeatable = toSignal(
    this.categoryForm.get('repeatable')!.valueChanges.pipe(
      startWith(this.categoryForm.get('repeatable')!.value)
    ),
    { initialValue: false }
  );

  isSubmitting = false;
  successMessage = '';
  errorMessage = '';

  constructor() {
    this.categoryForm.get('endTime')?.valueChanges.subscribe(() => this.clampDuration());
    this.categoryForm.get('attendanceTimeStart')?.valueChanges.subscribe(() => this.clampDuration());

    effect(() => {
      const repeatable = this.isRepeatable();

      const repeatableTypeCtrl = this.categoryForm.get('repeatableType');
      const repeatsOnSpecificDayCtrl = this.categoryForm.get('repeatsOnSpecificDay');
      const baseDateCtrl = this.categoryForm.get('baseDate');

      if (repeatable) {
        repeatableTypeCtrl?.setValidators([Validators.required]);
        repeatsOnSpecificDayCtrl?.setValidators([Validators.required]);
        baseDateCtrl?.setValidators([Validators.required]);
      } else {
        repeatableTypeCtrl?.clearValidators();
        repeatsOnSpecificDayCtrl?.clearValidators();
        baseDateCtrl?.clearValidators();

        repeatableTypeCtrl?.reset('');
        repeatsOnSpecificDayCtrl?.reset('');
        baseDateCtrl?.reset('');
      }

      repeatableTypeCtrl?.updateValueAndValidity();
      repeatsOnSpecificDayCtrl?.updateValueAndValidity();
      baseDateCtrl?.updateValueAndValidity();
    });
  }

  get f() {
    return this.categoryForm.controls;
  }

  attendanceDurationMax(): number {
    const end = this.f['endTime']?.value;
    const attStart = this.f['attendanceTimeStart']?.value;
    if (!end || !attStart) return 60;
    const available = this.toMins(end) - this.toMins(attStart);
    return Math.max(5, available);
  }

  private clampDuration(): void {
    const max = this.attendanceDurationMax();
    const durationCtrl = this.categoryForm.get('attendanceDuration');
    if (durationCtrl && durationCtrl.value > max) {
      durationCtrl.setValue(max);
    }
  }

  private toMins(t: string): number {
    const [h, m] = t.split(':').map(Number);
    return h * 60 + m;
  }

  attendanceOffsetPercent(): number {
    const start = this.categoryForm.get('startingTime')?.value;
    const end = this.categoryForm.get('endTime')?.value;
    const attStart = this.categoryForm.get('attendanceTimeStart')?.value;
    if (!start || !end || !attStart) return 0;
    const dur = this.toMins(end) - this.toMins(start);
    if (dur <= 0) return 0;
    return Math.max(0, Math.min(100, (this.toMins(start) - this.toMins(attStart)) / dur * 100));
  }

  attendanceOffsetLabel(): string {
    const start = this.categoryForm.get('startingTime')?.value;
    const attStart = this.categoryForm.get('attendanceTimeStart')?.value;
    if (!start || !attStart) return '';
    const diff = this.toMins(start) - this.toMins(attStart);
    if (diff <= 0) return '';
    return `${diff} min before`;
  }

  onSubmit(): void {
    if (this.categoryForm.invalid) {
      this.categoryForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';
    this.successMessage = '';

    const payload: CreateEventCategoryRequest = {
      ...this.categoryForm.value,
      startingTime: this.formatLocalTime(this.categoryForm.value.startingTime),
      endTime: this.formatLocalTime(this.categoryForm.value.endTime),
      attendanceTimeStart: this.formatLocalTime(this.categoryForm.value.attendanceTimeStart)
    };

    if (!payload.repeatable) {
      payload.repeatableType = null;
      payload.repeatsOnSpecificDay = null;
      payload.baseDate = null;
    }

    this.eventCategoryService.createCategory(payload).subscribe({
      next: () => {
        const categoryName = payload.name;
        this.notificationService.showFlash(
          'success',
          'Category created',
          `"${categoryName}" has been created successfully.`
        );
        this.eventCategoryService.notifyScheduleChanged();
        this.successMessage = 'Category created successfully!';
        this.isSubmitting = false;
        this.categoryCreated.emit();
      },
      error: (err: HttpErrorResponse) => {
        this.errorMessage = err?.error?.message || 'An error occurred while creating the category.';
        this.isSubmitting = false;
      }
    });
  }

  onCancel(): void {
    this.cancelled.emit();
  }

  private formatLocalTime(time: string): string {
    if (!time) {
      return time;
    }

    if (/^\d{2}:\d{2}:\d{2}$/.test(time)) {
      return time;
    }

    if (/^\d{2}:\d{2}$/.test(time)) {
      return `${time}:00`;
    }

    return time;
  }
}
