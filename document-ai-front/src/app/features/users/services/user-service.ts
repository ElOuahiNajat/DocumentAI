import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserResponse } from '../models/user-response';
import { PagedResponse } from '../models/paged-response'; 
import type { CreateUserRequest } from '../models/CreateUserRequest';


@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:9090/api/users';

  constructor(private http: HttpClient) {}

  getUsers(page: number, size: number): Observable<PagedResponse<UserResponse>> {
    return this.http.get<PagedResponse<UserResponse>>(`${this.apiUrl}?page=${page}&size=${size}`);
  }

   createUser(request: CreateUserRequest): Observable<void> {
    return this.http.post<void>(this.apiUrl, request);
   }
}
