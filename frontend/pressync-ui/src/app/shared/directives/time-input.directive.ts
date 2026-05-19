import { Directive, ElementRef, HostListener, inject } from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';

@Directive({
  selector: 'input[appTimeInput]',
  standalone: true,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: TimeInputDirective,
      multi: true,
    },
  ],
})
export class TimeInputDirective implements ControlValueAccessor {
  private el = inject(ElementRef<HTMLInputElement>);

  private onChange: (value: string) => void = () => {};
  private onTouched: () => void = () => {};

  writeValue(value: string): void {
    this.el.nativeElement.value = value || '';
  }

  registerOnChange(fn: (value: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  @HostListener('input')
  onInput(): void {
    const input = this.el.nativeElement;
    let digits = input.value.replace(/\D/g, '').slice(0, 4);

    if (digits.length >= 3) {
      digits = digits.slice(0, 2) + ':' + digits.slice(2);
    }

    const parts = digits.split(':');
    if (parts[0]) {
      let h = parseInt(parts[0], 10);
      if (h > 23) h = 23;
      parts[0] = String(h).padStart(2, '0');
    }
    if (parts[1]) {
      let m = parseInt(parts[1], 10);
      if (m > 59) m = 59;
      parts[1] = String(m).padStart(2, '0');
    }

    const formatted = parts.join(':');
    const cursor = input.selectionStart;
    input.value = formatted;
    this.onChange(formatted);

    if (cursor !== null) {
      input.setSelectionRange(cursor, cursor);
    }
  }

  @HostListener('blur')
  onBlur(): void {
    const input = this.el.nativeElement;
    const raw = input.value.replace(/\D/g, '');
    if (raw.length === 0) return;
    const padded = raw.padEnd(4, '0');
    const h = Math.min(parseInt(padded.slice(0, 2), 10), 23);
    const m = Math.min(parseInt(padded.slice(2, 4), 10), 59);
    const formatted = `${String(h).padStart(2, '0')}:${String(m).padStart(2, '0')}`;
    input.value = formatted;
    this.onChange(formatted);
    this.onTouched();
  }

  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent): void {
    const allowed = ['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab', 'Home', 'End'];
    if (allowed.includes(event.key)) return;
    if (event.ctrlKey && ['a', 'c', 'v', 'x'].includes(event.key.toLowerCase())) return;
    if (!/^\d$/.test(event.key)) {
      event.preventDefault();
    }
  }
}
