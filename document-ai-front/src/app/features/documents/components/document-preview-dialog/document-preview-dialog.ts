import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-document-preview',
  standalone: true,
  templateUrl: './document-preview-dialog.html',
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule
  ]
})
export class DocumentPreviewComponent implements OnInit {
  fileUrl: SafeResourceUrl | null = null;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { documentId: string },
    private http: HttpClient,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.loadDocumentPreview(this.data.documentId);
  }

  loadDocumentPreview(documentId: string): void {
    this.http
      .get(`http://localhost:9090/api/documents/${documentId}/preview`, {
        responseType: 'blob',
      })
      .subscribe(blob => {
        console.log("Blob reçu :", blob);

        const pdfBlob = new Blob([blob], { type: 'application/pdf' });

        this.fileUrl = this.sanitizer.bypassSecurityTrustResourceUrl(URL.createObjectURL(pdfBlob));
      });
  }
}
