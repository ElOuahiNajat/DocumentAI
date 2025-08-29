import { Injectable, inject } from "@angular/core"
import { HttpClient } from "@angular/common/http"
import type { Observable } from "rxjs"
import type { AuthenticationRequest } from "../models/AuthenticationRequest"
import type { AuthenticationResponse } from "../models/AuthenticationResponse"
import { environment } from "../../../../environments/environment"

@Injectable({
  providedIn: "root",
})
export class AuthService {
  private readonly apiUrl = `${environment.apiUrl}/auth`
  private http = inject(HttpClient)

  login(credentials: AuthenticationRequest): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(`${this.apiUrl}/login`, credentials)
  }

  logout(): void {
    this.removeToken()
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem("authToken")
  }

  getToken(): string | null {
    return localStorage.getItem("authToken")
  }

  setToken(token: string): void {
    localStorage.setItem("authToken", token)
  }

  removeToken(): void {
    localStorage.removeItem("authToken")
  }

  getCurrentUser(): any {
    const token = this.getToken()
    if (token) {
      try {
        // Decode JWT token to get user info (basic implementation)
        const payload = JSON.parse(atob(token.split(".")[1]))
        return payload
      } catch (error) {
        console.error("Error decoding token:", error)
        return null
      }
    }
    return null
  }

  getCurrentUserEmail(): string | null {
    const user = this.getCurrentUser();
    if (user && user.sub) {
      return user.sub;
    }
    return null;
  }
  getUserRole(): string | null {
    const user = this.getCurrentUser()
    if (user && user.role) {
      return user.role;
    }
    return null
  }
}
