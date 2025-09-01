import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserResponse } from '../models/user-response';
import type { CreateUserRequest } from '../models/CreateUserRequest';
import {PaginatedListResponse} from '../../../shared/components/PaginatedListResponse';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:9090/api/users';

  constructor(private http: HttpClient) {}

  getUsers(page: number, size: number): Observable<PaginatedListResponse<UserResponse>> {
    return this.http.get<PaginatedListResponse<UserResponse>>(`${this.apiUrl}?page=${page}&size=${size}`);
  }

   createUser(request: CreateUserRequest): Observable<void> {
    return this.http.post<void>(this.apiUrl, request);
   }

    deleteUser(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }


  updateUser(id: string, request: any): Observable<UserResponse> {
    return this.http.put<UserResponse>(`${this.apiUrl}/${id}`, request);
  }

  exportUsersToCSV(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export/csv`, {
      responseType: 'blob'
  });
}
}
