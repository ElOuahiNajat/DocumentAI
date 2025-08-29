import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class AdminGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    if (this.authService.isAuthenticated() && this.authService.getUserRole() === 'ADMIN') {
      return true;
    }
    // Not admin → redirect somewhere safe
    this.router.navigate(['/documents']);
    return false;
  }
}
