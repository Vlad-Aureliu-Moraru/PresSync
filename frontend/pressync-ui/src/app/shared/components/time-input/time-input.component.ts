import { Component, Input, inject } from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';

@Component({
  selector: 'app-time-input',
  standalone: true,
  template: `
    <div class="time-group" [class.is-invalid]="invalid">
      <input #h type="text" inputmode="numeric" maxlength="2" autocomplete="off"
             [value]="hours"
             (input)="onHourInput($event)"
             (keydown)="onHourKeydown($event, m)"
             (blur)="onHourBlur()"
             placeholder="HH">
      <span class="time-sep">:</span>
      <input #m type="text" inputmode="numeric" maxlength="2" autocomplete="off"
             [value]="minutes"
             (input)="onMinuteInput($event)"
             (keydown)="onMinuteKeydown($event, h)"
             (blur)="onMinuteBlur()"
             placeholder="MM">
    </div>
  `,
  styles: [`
    .time-group {
      display: inline-flex;
      align-items: center;
      gap: 2px;
      background: var(--bg-primary);
      border: 1px solid var(--border);
      border-radius: 6px;
      padding: 0.65rem 0.75rem;
      transition: border-color 0.2s ease, box-shadow 0.2s ease;
    }

    .time-group:focus-within {
      border-color: var(--accent);
      box-shadow: var(--glow-effect);
    }

    .time-group.is-invalid {
      border-color: var(--error);
      background: var(--error-bg);
    }

    .time-group.is-invalid:focus-within {
      box-shadow: 0 0 0 3px var(--error-bg);
    }

    .time-group input {
      width: 24px;
      border: none;
      outline: none;
      background: transparent;
      color: var(--text-main);
      font-family: 'SF Mono', 'Cascadia Code', 'Fira Code', 'Consolas', monospace;
      font-size: 1rem;
      font-weight: 500;
      text-align: center;
      padding: 0;
      cursor: text;
    }

    .time-group input::placeholder {
      color: var(--text-muted);
      opacity: 0.5;
    }

    .time-sep {
      color: var(--text-secondary);
      font-weight: 600;
      font-size: 1rem;
      line-height: 1;
      user-select: none;
    }
  `],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: TimeInputComponent,
      multi: true,
    },
  ],
})
export class TimeInputComponent implements ControlValueAccessor {
  @Input() invalid = false;

  hours = '';
  minutes = '';

  private onChange: (value: string) => void = () => {};
  private onTouched: () => void = () => {};

  writeValue(value: string): void {
    if (!value) {
      this.hours = '';
      this.minutes = '';
      return;
    }
    const parts = value.split(':');
    this.hours = parts[0] || '';
    this.minutes = parts[1] || '';
  }

  registerOnChange(fn: (value: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    const inputs = document.querySelectorAll('app-time-input input');
    // Handled by form control disabled state indirectly
  }

  private emit(): void {
    const h = this.hours.padStart(2, '0');
    const m = this.minutes.padStart(2, '0');
    this.onChange(`${h}:${m}`);
    this.onTouched();
  }

  onHourInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    let val = input.value.replace(/\D/g, '').slice(0, 2);
    if (val.length === 2) {
      let h = parseInt(val, 10);
      if (h > 23) h = 23;
      val = String(h).padStart(2, '0');
      this.hours = val;
      this.emit();
      const form = input.closest('.time-group');
      if (form) {
        const next = form.querySelector('input:last-child') as HTMLInputElement;
        next?.focus();
        next?.select();
      }
      return;
    }
    this.hours = val;
    this.emit();
  }

  onHourBlur(): void {
    if (this.hours.length === 1) {
      let h = parseInt(this.hours, 10);
      if (h > 23) h = 23;
      this.hours = String(h).padStart(2, '0');
      this.emit();
    }
  }

  onHourKeydown(event: KeyboardEvent, minuteInput: HTMLInputElement): void {
    if (event.key === 'ArrowRight' && this.hours.length >= 2) {
      minuteInput.focus();
      minuteInput.select();
    }
    if (!/^\d$/.test(event.key) && !['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab', 'Home', 'End'].includes(event.key) && !(event.ctrlKey && ['a', 'c', 'v', 'x'].includes(event.key.toLowerCase()))) {
      event.preventDefault();
    }
  }

  onMinuteInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    let val = input.value.replace(/\D/g, '').slice(0, 2);
    if (val.length === 2) {
      let m = parseInt(val, 10);
      if (m > 59) m = 59;
      val = String(m).padStart(2, '0');
      this.minutes = val;
      this.emit();
      return;
    }
    this.minutes = val;
    this.emit();
  }

  onMinuteBlur(): void {
    if (this.minutes.length === 1) {
      let m = parseInt(this.minutes, 10);
      if (m > 59) m = 59;
      this.minutes = String(m).padStart(2, '0');
      this.emit();
    }
  }

  onMinuteKeydown(event: KeyboardEvent, hourInput: HTMLInputElement): void {
    if (event.key === 'ArrowLeft' && (event.target as HTMLInputElement).selectionStart === 0) {
      hourInput.focus();
      hourInput.setSelectionRange(hourInput.value.length, hourInput.value.length);
    }
    if (event.key === 'Backspace' && (event.target as HTMLInputElement).value === '') {
      hourInput.focus();
      hourInput.setSelectionRange(hourInput.value.length, hourInput.value.length);
    }
    if (!/^\d$/.test(event.key) && !['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab', 'Home', 'End'].includes(event.key) && !(event.ctrlKey && ['a', 'c', 'v', 'x'].includes(event.key.toLowerCase()))) {
      event.preventDefault();
    }
  }
}
