import { inject, Injectable } from "@angular/core"
import { HttpClient, HttpParams } from "@angular/common/http"
import type { Observable } from "rxjs"
import type { DocumentResponse } from "../models/DocumentResponse"
import {PaginatedListResponse} from "../../../shared/components/PaginatedListResponse";
import {environment} from "../../../../environments/environment";
import {UpdateDocumentRequest} from '../models/UpdateDocumentRequest';

@Injectable({
  providedIn: "root",
})
export class DocumentService {
  private readonly apiUrl = `${environment.apiUrl}/documents`;
  private http = inject(HttpClient)

  getDocumentsPaginated(
    page: number,
    size: number,
    searchTerm: string | null = null,
    updatedAtStart: string | null = null,
    updatedAtEnd: string | null = null,
    createdAtStart: string | null = null,
    createdAtEnd: string | null = null,
    selectedSizeOperator: string | null = null,
    sizeValue: number | null = null,
  ): Observable<PaginatedListResponse<DocumentResponse>> {
    let params = new HttpParams().set("page", page.toString()).set("size", size.toString())
    // Add parameters only if they are not null
    if (searchTerm) {
      params = params.set("searchTerm", searchTerm)
    }
    if (selectedSizeOperator) {
      params = params.set("fileSizeComparator", selectedSizeOperator)
    }
    if (sizeValue !== null) {
      params = params.set("fileSize", sizeValue.toString())
    }
    if (createdAtStart) {
      params = params.set("createdAtStart", createdAtStart)
    }
    if (createdAtEnd) {
      params = params.set("createdAtEnd", createdAtEnd)
    }
    if (updatedAtStart) {
      params = params.set("updatedAtStart", updatedAtStart)
    }
    if (updatedAtEnd) {
      params = params.set("updatedAtEnd", updatedAtEnd)
    }
    return this.http.get<PaginatedListResponse<DocumentResponse>>(this.apiUrl, { params })

  }

   deleteDocument(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  addDocument(formData: FormData) {
    return this.http.post(`${this.apiUrl}`, formData);
  }
  updateDocument(id: string, request: UpdateDocumentRequest): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, request)
  }

}
