import { Component, type OnInit, Inject } from "@angular/core"
import { CommonModule } from "@angular/common"
import { FormsModule } from "@angular/forms"
import { MatDialogActions, MatDialogContent,  MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatInputModule } from "@angular/material/input"
import { MatButtonModule } from "@angular/material/button"
import type { DocumentResponse } from "../../models/DocumentResponse"
import type { UpdateDocumentRequest } from "../../models/UpdateDocumentRequest"

@Component({
  selector: "app-document-edit",
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDialogActions,
    MatDialogContent,
  ],
  templateUrl: "./document-edit.html",
  styleUrls: ["./document-edit.css"],
})
export class DocumentEditComponent implements OnInit {
  editableDocument: UpdateDocumentRequest = {
    title: "",
    author: "",
    description: "",
  };

  constructor(
    public dialogRef: MatDialogRef<DocumentEditComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { document: DocumentResponse }, // <-- CORRECTED LINE
  ) {}

  ngOnInit(): void {
    if (this.data.document) {
      this.editableDocument = {
        title: this.data.document.title,
        author: this.data.document.author,
        description: this.data.document.description,
      }
    }
  }

  onSave(): void {
    this.dialogRef.close(this.editableDocument)
  }

  onCancel(): void {
    this.dialogRef.close()
  }
}
