export interface PaginatedListResponse<T> {
  content: T[]
  page:{
    totalPages: number;
    totalElements: number;
    size: number;
    number: number;
  }
}
