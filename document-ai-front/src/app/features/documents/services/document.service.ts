import { inject, Injectable } from "@angular/core"
import { HttpClient, HttpParams } from "@angular/common/http"
import type { Observable } from "rxjs"
import type { DocumentResponse } from "../models/DocumentResponse"
import {PaginatedListResponse} from "../../../shared/components/PaginatedListResponse";
import {environment} from "../../../../environments/environment";

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
    return this.http.get<PaginatedListResponse<DocumentResponse>>(this.apiUrl, { params })
  }
}
