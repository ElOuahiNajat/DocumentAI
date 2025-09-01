import { Routes } from '@angular/router';
import { AdminGuard } from './features/auth/services/admin.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full',
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login').then((m) => m.LoginComponent),
    title: 'Login - DocumentAi',
  },
  {
    path: 'dashboard',
    loadComponent: () =>
      import('./features/dashboard/pages/dashboard-page/dashboard-page').then(
        (m) => m.DashboardPage
      ),
    canActivate: [AdminGuard], // <-- uniquement accessible par admin
    title: 'Dashboard - DocumentAi',
  },
  {
    path: 'documents',
    loadComponent: () =>
      import('./features/documents/pages/document-page/document-page').then(
        (m) => m.DocumentPage
      ),
  },
  {
    path: 'users',
    loadComponent: () =>
      import('./features/users/pages/users-list/users-list').then(
        (m) => m.UsersList
      ),
    canActivate: [AdminGuard],
  },
  {
    path: '**',
    redirectTo: 'dashboard',
    pathMatch: 'full',
  },
];
