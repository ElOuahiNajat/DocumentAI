import { Component, EventEmitter, Input, Output } from "@angular/core"
import { CommonModule } from "@angular/common"
import { FormsModule } from "@angular/forms"

export interface DocumentFilterData {
  searchTerm: string
  updatedAtStart: string | null
  updatedAtEnd: string | null
  createdAtStart: string | null
  createdAtEnd: string | null
  selectedSizeOperator: string
  sizeValue: number | null
}

@Component({
  selector: "app-document-filter",
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl:"./document-filter.html"
})
export class DocumentFilterComponent {
  @Input() filterData: DocumentFilterData = {
    searchTerm: "",
    updatedAtStart: null,
    updatedAtEnd: null,
    createdAtStart: null,
    createdAtEnd: null,
    selectedSizeOperator: "GREATER_THAN",
    sizeValue: null
  }

  @Output() filterChange = new EventEmitter<DocumentFilterData>()
  @Output() addDocument = new EventEmitter<void>()

  onFilterChange(): void {
    this.filterChange.emit({ ...this.filterData })
  }

  onAddDocument(): void {
    this.addDocument.emit()
  }
}


