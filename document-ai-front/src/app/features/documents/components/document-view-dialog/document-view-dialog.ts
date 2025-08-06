import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DocumentResponse } from '../../models/DocumentResponse';
import { FeedbackResponse } from '../../models/FeedbackResponse';
import { FeedbackRequest } from '../../models/FeedbackRequest';
import { DocumentService } from '../../services/document.service';
import { CommonModule, NgClass } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-document-view-dialog',
   standalone: true,
  templateUrl: './document-view-dialog.html',
  styleUrls: ['./document-view-dialog.css'],
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatSnackBarModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    NgClass
  ]
})
export class DocumentViewDialog implements OnInit {
  feedbacks: FeedbackResponse[] = [];
  feedbackContent: string = '';
  feedbackRating: number = 0;

  isSubmitting = false;

  stars = [1, 2, 3, 4, 5];

  constructor(
    @Inject(MAT_DIALOG_DATA) public document: DocumentResponse,
    private documentService: DocumentService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<DocumentViewDialog>
  ) {}

  ngOnInit(): void {
    this.document.createdAt = this.convertToDate(this.document.createdAt) as any;
    this.loadFeedbacks();
  }

  setRating(value: number): void {
    this.feedbackRating = value;
  }

  isFeedbackValid(): boolean {
    return !!this.feedbackContent.trim() || this.feedbackRating > 0;
  }

  public convertToDate(input: any): Date {
    if (Array.isArray(input) && input.length >= 3) {
      const [year, month, day, hour = 0, min = 0, sec = 0, ms = 0] = input;
      return new Date(year, month - 1, day, hour, min, sec, Math.floor(ms / 10000));
    }
    return input instanceof Date ? input : new Date();
  }

  loadFeedbacks(): void {
    this.documentService.getDocumentById(this.document.id).subscribe({
      next: (data: DocumentResponse) => {
        this.feedbacks = (data.feedbacks ?? []).map(f => ({
          ...f,
          createdAt: this.convertToDate(f.createdAt)
        }));
      },
      error: (err) => console.error(err)
    });
  }

  submitFeedback(): void {
    if (!this.feedbackContent && this.feedbackRating === 0) return;

    const request: FeedbackRequest = {
      content: this.feedbackContent,
      note: this.feedbackRating
    };

    this.documentService.addFeedback(this.document.id, request).subscribe({
      next: () => {
        this.feedbackContent = '';
        this.feedbackRating = 0;
        this.loadFeedbacks();
        this.snackBar.open('Comment sent successfully !', 'Close', {
          duration: 3000,
          verticalPosition: 'top'
        });
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open('Error sending comment.', 'Close', {
          duration: 3000,
          verticalPosition: 'top'
        });
      }
    });
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

}
