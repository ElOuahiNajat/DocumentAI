import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatbotService {
  private apiUrl = 'http://127.0.0.1:5000/chat'; // URL de ton backend Flask

  constructor(private http: HttpClient) {}

  sendMessage(question: string): Observable<any> {
    return this.http.post<any>(this.apiUrl, { question });
  }
}
