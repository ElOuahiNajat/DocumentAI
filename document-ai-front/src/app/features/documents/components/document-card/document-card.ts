import { Component, Input, Output, EventEmitter } from "@angular/core"
import { CommonModule } from "@angular/common"
import type { DocumentResponse } from "../../models/DocumentResponse" // Adjusted path
import { DocumentButtonComponent } from "../document-button/document-button" // Import the new component
import { MatCardModule } from "@angular/material/card" // Import MatCardModule
import { MatButtonModule } from "@angular/material/button" // Import MatButtonModule
import { MatIconModule } from "@angular/material/icon" // Import MatIconModule
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: "app-document-card",
  standalone: true,
  imports: [CommonModule, DocumentButtonComponent, MatCardModule, MatButtonModule, MatIconModule,  MatProgressSpinnerModule], // Add MatCardModule to imports
  templateUrl: "./document-card.html",
  styleUrls: ["./document-card.css"], // Changed back to .css
})
export class DocumentCardComponent {
  @Input({ required: true }) document!: DocumentResponse
  @Output() edit = new EventEmitter<DocumentResponse>()
  @Output() delete = new EventEmitter<DocumentResponse>()
  @Output() download = new EventEmitter<DocumentResponse>()
  @Output() detail = new EventEmitter<DocumentResponse>()
  @Output() share = new EventEmitter<DocumentResponse>()
  @Output() print = new EventEmitter<DocumentResponse>()
  @Output() preview = new EventEmitter<DocumentResponse>()

  onEdit(): void {
    this.edit.emit(this.document)
  }

  onDelete(): void {
    this.delete.emit(this.document)
  }

  onDownload(): void {
    this.download.emit(this.document)
  }

  onDetail(): void {
    this.detail.emit(this.document)
  }

  onShare(): void {
    console.log("Share document:", this.document.title)
    this.share.emit(this.document)
  }

  onPrint(): void {
    console.log("Print document:", this.document.title)
    this.print.emit(this.document)
  }

  onPreview(): void {
    this.preview.emit(this.document)
  }

}
