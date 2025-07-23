import { Component, type OnInit } from "@angular/core"
import { CommonModule } from "@angular/common"
import { DocumentCardComponent } from "../document-card/document-card"
import type { DocumentResponse } from "../../models/DocumentResponse"
import { FormsModule } from "@angular/forms"
import { DocumentService } from "../../services/document.service"
import { PaginatedListResponse } from "../../../../shared/components/PaginatedListResponse"
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator'
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {Subscription} from "rxjs";

@Component({
  selector: "app-document-list",
  standalone: true,
  imports: [CommonModule, DocumentCardComponent, FormsModule, MatPaginatorModule, MatProgressSpinner],
  templateUrl: "./document-list.html",
})
export class DocumentListComponent implements OnInit {
  private documentsubscription?:Subscription;
  filteredDocuments: DocumentResponse[] = []
  searchTerm = ""
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
  pageSize = 10 // Renamed from itemsPerPage to match mat-paginator
  totalDocuments = 0 // Renamed from totalElements to match mat-paginator
  totalPages = 1

  constructor(private documentService: DocumentService) {}

  ngOnInit() {
    this.loadDocuments()
  }

  loadDocuments() {
    this.loading = true
    // @ts-ignore
     this.documentsubscription= this.documentService
        .getDocumentsPaginated(
            this.currentPage - 1,
            this.pageSize,
            this.searchTerm,
            this.updatedAtStart,
            this.updatedAtEnd,
            this.createdAtStart,
            this.createdAtEnd,
            this.selectedSizeOperator,
            this.sizeValue ? this.sizeValue * 1024 * 1024 : null, // Convert MB to bytes
        )
        .subscribe({
          next: (response: PaginatedListResponse<DocumentResponse>) => {
            this.filteredDocuments = response.content
            this.totalPages = response.totalPages
            this.totalDocuments = response.totalElements // Map to totalDocuments for paginator
            this.loading = false
          },
          error: (error: any) => {
            console.error("Error loading documents:", error)
            this.loading = false
          },
          complete:()=>{
            this.documentsubscription?.unsubscribe();
            this.loading=false;
          }
        })
  }

  onSearch() {
    this.currentPage = 1
    this.loadDocuments()
  }

  // New method for Angular Material Paginator
  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex + 1 // Convert from 0-based to 1-based
    this.pageSize = event.pageSize
    this.loadDocuments()
  }

  // Keep existing methods for compatibility (can be removed if not used elsewhere)
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

  onEditDocument(document: DocumentResponse): void {
    console.log("Edit document:", document.title)
  }

  onDetailDocument(document: DocumentResponse): void {
    console.log("View details for document:", document.title)
  }

  trackByDocumentId(index: number, document: DocumentResponse): string {
    return document.id
  }
}
