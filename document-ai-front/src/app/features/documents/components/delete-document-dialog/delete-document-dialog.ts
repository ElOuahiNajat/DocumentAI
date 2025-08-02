import { Component, Inject } from "@angular/core"
import { MatButtonModule } from "@angular/material/button"
import { MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle, MAT_DIALOG_DATA } from "@angular/material/dialog"
import { CommonModule } from "@angular/common"

export interface DeleteDocumentDialogData {
  documentTitle: string
}

@Component({
  selector: "app-delete-document-dialog",
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatDialogActions, MatDialogContent, MatDialogTitle],
  templateUrl: "./delete-document-dialog.html",
  styleUrls: ["./delete-document-dialog.css"],
})
export class DeleteDocumentDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<DeleteDocumentDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DeleteDocumentDialogData
  ) {}

  ConfirmDelete(): void {
    this.dialogRef.close(true)
  }

  onCancel(): void {
    this.dialogRef.close(false)
  }
}