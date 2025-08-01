import { Component, type OnInit, type OnDestroy } from "@angular/core"
import { MatSnackBar } from '@angular/material/snack-bar';
import { CommonModule } from "@angular/common"
import { DocumentCardComponent } from "../../components/document-card/document-card"
import { DocumentFilterComponent, type DocumentFilterData } from "../../components/document-filter/document-filter"
import type { DocumentResponse } from "../../models/DocumentResponse"
import { FormsModule } from "@angular/forms"
import { DocumentService } from "../../services/document.service"
import type { PaginatedListResponse } from "../../../../shared/components/PaginatedListResponse"
import { MatPaginatorModule, type PageEvent } from "@angular/material/paginator"
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner"
import { MatDialog, MatDialogModule } from "@angular/material/dialog"
import type { Subscription } from "rxjs"
import { AddDocumentDialog } from '../add-document-dialog/add-document-dialog';

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
    FormsModule
  ],
  templateUrl:"document-list.html",
})
export class DocumentListComponent implements OnInit, OnDestroy {
  private documentSubscription?: Subscription
  filteredDocuments: DocumentResponse[] = []
  loading = true
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
    console.log("Delete document:", document.title)
  }

  onDownloadDocument(document: DocumentResponse): void {
    console.log("Download document:", document.title)
  }

  onDetailDocument(document: DocumentResponse): void {
    console.log("View details for document:", document.title)
  }

  trackByDocumentId(index: number, document: DocumentResponse): string {
    return document.id
  }

  onEditDocument($event: DocumentResponse) {

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
}
