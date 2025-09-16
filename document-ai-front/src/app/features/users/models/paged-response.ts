// src/app/models/paged-response.ts
export interface PagedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages?: number;
  size?: number;
  number?: number;
}
