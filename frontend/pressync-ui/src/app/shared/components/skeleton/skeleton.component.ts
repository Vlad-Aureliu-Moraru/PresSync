import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-skeleton',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './skeleton.component.html',
  styleUrl: './skeleton.component.scss'
})
export class SkeletonComponent {
  @Input() width = '100%';
  @Input() height = '1rem';
  @Input() borderRadius = '8px';
  @Input() count = 1;
  @Input() variant: 'text' | 'card' | 'circle' | 'chart' = 'text';

  get range(): number[] {
    return Array.from({ length: this.count }, (_, i) => i);
  }
}
