import { Component, Input, Output, EventEmitter } from "@angular/core"
import { CommonModule } from "@angular/common"
import { MatButtonModule } from "@angular/material/button" // Import MatButtonModule
import { MatIconModule } from "@angular/material/icon" // Import MatIconModule

@Component({
  selector: "app-document-button",
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatIconModule], // Add MatButtonModule and MatIconModule
  template: `
    <button
      mat-icon-button
      (click)="onClick()"
      [attr.aria-label]="ariaLabel"
      class="text-gray-600 hover:text-gray-900 hover:bg-gray-100 transition-colors"
    >
      <mat-icon [ngClass]="iconClass">{{ icon }}</mat-icon>
    </button>
  `,
  styleUrls: ["./document-button.css"],
})
export class DocumentButtonComponent {
  @Input({ required: true }) icon!: string
  @Input() ariaLabel = ""
  @Input() iconClass = "" // Optional class for icon color (e.g., 'text-red-500')
  @Output() buttonClick = new EventEmitter<void>()
  
    onClick(): void {
      this.buttonClick.emit();
  }
}
