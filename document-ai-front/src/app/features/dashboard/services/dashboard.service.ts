
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface DashboardStats {
    totalDocuments: number;
    totalUsers: number;
    documentsPdf: number;
    documentsWord: number;
    documentsExcel: number;
    documentsRecent: number;
    documentsWithFeedback: number;
    documentsWithoutFeedback:number
}


@Injectable({
    providedIn: 'root'
})
export class DashboardService {
    private apiUrl = 'http://localhost:9090/api/dashboard'; // adapte si ton backend tourne ailleurs

    constructor(private http: HttpClient) {}

    getStats(): Observable<DashboardStats> {
        return this.http.get<DashboardStats>(this.apiUrl);
    }
}
