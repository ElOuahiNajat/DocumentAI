import { Component, type OnInit, type OnDestroy } from "@angular/core"
import { CommonModule } from "@angular/common"
import { DocumentCardComponent } from "../../components/document-card/document-card"
import { DocumentFilterComponent, type DocumentFilterData } from "../../components/document-filter/document-filter"
import type { DocumentResponse } from "../../models/DocumentResponse"
import { FormsModule } from "@angular/forms"
import { DocumentService } from "../../services/document.service"
import { PaginatedListResponse } from "../../../../shared/components/PaginatedListResponse"
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator'
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner"
import {Subscription} from "rxjs";
import { DeleteDocumentDialogComponent } from "../../components/delete-document-dialog/delete-document-dialog"  
import { MatDialog, MatDialogModule } from "@angular/material/dialog"
import type { Subscription } from "rxjs"
import { AddDocumentDialog } from '../add-document-dialog/add-document-dialog';
import {DocumentEditComponent} from '../document-edit/document-edit';
import {UpdateDocumentRequest} from '../../models/UpdateDocumentRequest';
import {UpdateDocumentDialogComponent} from '../update-document-dialog/update-document-dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: "app-document-list",
  standalone: true,
  imports: [
    CommonModule,
    DocumentCardComponent,
    DocumentFilterComponent,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatDialogModule,
    FormsModule,
    MatSnackBarModule
  ],

  templateUrl:"document-list.html",
})
export class DocumentListComponent implements OnInit, OnDestroy {
  private documentSubscription?: Subscription
  private downloadSubscription?: Subscription;
  private deleteDocumentSubscription?: Subscription;
  filteredDocuments: DocumentResponse[] = []
  loading = true
  // Removed selectedTimeFilter and selectedFileTypeFilter

  // New filter properties
  updatedAtStart: string | null = null
  updatedAtEnd: string | null = null
  createdAtStart: string | null = null
  createdAtEnd: string | null = null
  selectedSizeOperator = "gt" // Default to 'Greater than'
  sizeValue: number | null = null // Size in MB

  // Pagination properties for Angular Material Paginator
  currentPage = 1
  pageSize = 10
  totalDocuments = 0
  totalPages = 1

  filterData: DocumentFilterData = {
    searchTerm: "",
    updatedAtStart: null,
    updatedAtEnd: null,
    createdAtStart: null,
    createdAtEnd: null,
    selectedSizeOperator: "GREATER_THAN",
    sizeValue: null
  }
  

  constructor(
    private documentService: DocumentService,
    public dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.loadDocuments()
  }
 
  ngOnDestroy(): void {
    this.documentSubscription?.unsubscribe()
    this.deleteDocumentSubscription?.unsubscribe()
  }

  onFilterChange(filterData: DocumentFilterData): void {
    this.filterData = { ...filterData }
    this.currentPage = 1
    this.loadDocuments()
  }

  onAddDocument(): void {
    // Handle add document logic here
    console.log("Add new document")
  }

  loadDocuments() {
    this.loading = true
    const fileSizeInKB = this.filterData.sizeValue
      ? this.filterData.sizeValue * 1024 : null // convert MB to KB before sending to backend

    this.documentSubscription = this.documentService
      .getDocumentsPaginated(
        this.currentPage - 1,
        this.pageSize,
        this.filterData.searchTerm,
        this.filterData.updatedAtStart,
        this.filterData.updatedAtEnd,
        this.filterData.createdAtStart,
        this.filterData.createdAtEnd,
        this.filterData.selectedSizeOperator,
        fileSizeInKB,
      )
      .subscribe({
        next: (response: PaginatedListResponse<DocumentResponse>) => {
          this.filteredDocuments = response.content
          this.totalPages = response.totalPages
          this.totalDocuments = response.totalElements
          this.loading = false
        },
        error: (error: any) => {
          console.error("Error loading documents:", error)
          this.loading = false
        },
        complete: () => {
          this.documentSubscription?.unsubscribe()
          this.loading = false
        },
      })
  }
  onEditDocument(document: DocumentResponse): void {
    const dialogRef = this.dialog.open(DocumentEditComponent, {
      width: "600px",
      data: { document: document },
    })

    dialogRef.afterClosed().subscribe((result: UpdateDocumentRequest | undefined) => {
      if (result) {
        // Show confirmation before saving changes using UpdateDocumentDialogComponent
        const saveConfirmDialogRef = this.dialog.open(UpdateDocumentDialogComponent, {
          width: "90vw",
          maxWidth: "400px",
          minWidth: "300px",
          data: {
            documentTitle: result.title || document.title
          }
        })

        saveConfirmDialogRef.afterClosed().subscribe((saveConfirmed: boolean) => {
          if (saveConfirmed) {
            this.saveDocument(document.id, result)
          }
        })
      } else {
        console.log("Edit dialog closed without saving or confirmation cancelled.")
      }
    })
  }

  private saveDocument(documentId: string, updateRequest: UpdateDocumentRequest): void {
    this.documentService.updateDocument(documentId, updateRequest).subscribe({
      next: () => {
        this.snackBar.open(`Document "${updateRequest.title}" updated successfully`, 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['success-snackbar']
        })
        this.loadDocuments()
      },
      error: (error: any) => {
        console.error("Error updating document:", error)
        this.snackBar.open(
          `Failed to update document "${updateRequest.title}". ${error.message || "Unknown error"}`,
          'Close',
          {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass: ['error-snackbar']
          }
        )
      },
    })
  }


  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex + 1
    this.pageSize = event.pageSize
    this.loadDocuments()
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page
      this.loadDocuments()
    }
  }

  getPages(): number[] {
    const pages: number[] = []
    for (let i = 1; i <= this.totalPages; i++) {
      pages.push(i)
    }
    return pages
  }

 onDeleteDocument(document: DocumentResponse): void {
    const dialogRef = this.dialog.open(DeleteDocumentDialogComponent, {
      maxWidth: '95vw',
      width: 'auto',
      height: 'auto',
      disableClose: true,
      panelClass: 'responsive-dialog',
      data: {
        documentTitle: document.title
      }
    })

    dialogRef.afterClosed().subscribe((result: boolean) => {
      if (result === true) {
        this.performDocumentDeletion(document)
      }
    })
  }

  private performDocumentDeletion(document: DocumentResponse): void {
    this.deleteDocumentSubscription?.unsubscribe()

    this.deleteDocumentSubscription = this.documentService.deleteDocument(document.id).subscribe({
      next: () => {
        this.snackBar.open(`Document "${document.title}" deleted successfully`, 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['success-snackbar']
        })

        this.loadDocuments()
      },
      error: (err) => {
        console.error('Error deleting document:', err)

        const snackBarRef = this.snackBar.open(
          `Failed to delete document "${document.title}". Would you like to retry?`,
          'Retry',
          {
            duration: 8000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass: ['error-snackbar']
          }
        )

        snackBarRef.onAction().subscribe(() => {
          this.performDocumentDeletion(document)
        })
      },
      complete: () => {
        this.deleteDocumentSubscription?.unsubscribe()
      }
    })
  }
  onDetailDocument(document: DocumentResponse): void {
    console.log("View details for document:", document.title)
  }

  trackByDocumentId(index: number, document: DocumentResponse): string {
    return document.id
  }

  openAddDocumentDialog(): void {
    const dialogRef = this.dialog.open(AddDocumentDialog, {
      maxWidth: '90vw',
      panelClass: 'custom-dialog-container',
    });

    dialogRef.afterClosed().subscribe((formData: FormData) => {
      if (formData) {
        this.documentService.addDocument(formData).subscribe({
          next: () => {
            this.snackBar.open('Document added successfully!', 'Close', {
              duration: 3000,
              panelClass: ['snackbar-success']
            });
            this.loadDocuments();
          },
          error: (err) => {
            console.error('Error adding document:', err);
            this.snackBar.open('Failed to add document.', 'Close', {
              duration: 3000,
              panelClass: ['snackbar-error']
            });
          }
        });
      }
    });
  }

 onDownloadDocument(doc: DocumentResponse): void {
  this.downloadSubscription = this.documentService.downloadDocumentWithHeaders(doc.id).subscribe({
    next: (response) => {
      const blob = response.body as Blob;
      const contentDisposition = response.headers.get('Content-Disposition');
      let fileName = doc.title;

      if (contentDisposition) {
        const matches = /filename="(.+?)"/.exec(contentDisposition);
        if (matches && matches[1]) {
          fileName = matches[1];
        }
      }

      this.snackBar.open(`Download of "${fileName}" started.`, 'Close', {
        duration: 2000,
        panelClass: ['snackbar-info']
      });

      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = fileName;
      a.click();
      window.URL.revokeObjectURL(url);

      setTimeout(() => {
        this.snackBar.open(`Download of "${fileName}" succeeded.`, 'Close', {
          duration: 3000,
          panelClass: ['snackbar-success']
        });
      }, 1000); 
    },
    error: (error) => {
      console.error('Download error:', error);
      this.snackBar.open('Download failed. Please try again.', 'Close', {
        duration: 5000,
        panelClass: ['snackbar-error']
      });
    },
    complete: () => {
      this.downloadSubscription?.unsubscribe();
    }
  });
}

}
