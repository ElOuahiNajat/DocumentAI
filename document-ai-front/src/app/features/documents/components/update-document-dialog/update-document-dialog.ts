import { Component, Inject } from "@angular/core"
import { MatButtonModule } from "@angular/material/button"
import { MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle, MAT_DIALOG_DATA } from "@angular/material/dialog"
import { CommonModule } from "@angular/common"

export interface UpdateDocumentDialogData {
  documentTitle: string
}

@Component({
  selector: "app-update-document-dialog",
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatDialogActions, MatDialogContent, MatDialogTitle],
  templateUrl: "./update-document-dialog.html",
  styleUrls: ["./update-document-dialog.css"],
})
export class UpdateDocumentDialogComponent {
  constructor(
      public dialogRef: MatDialogRef<UpdateDocumentDialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data: UpdateDocumentDialogData
  ) {}

  ConfirmUpdate(): void {
    this.dialogRef.close(true)
  }

  onCancel(): void {
    this.dialogRef.close(false)
  }
}
