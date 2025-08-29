import { Routes } from '@angular/router';
import { DocumentPage } from './features/documents/pages/document-page/document-page';
import { UsersList } from './features/users/pages/users-list/users-list';
import { AdminGuard } from './features/auth/services/admin.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./features/auth/login/login').then((m) => m.LoginComponent),
    title: 'Login - DocumentAi',
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login').then((m) => m.LoginComponent),
    title: 'Login - DocumentAi',
  },
  {
    path: 'documents',
    component: DocumentPage,
    children: [
      {
        path: '',
        loadChildren: () =>
          import('./features/documents/documents.routes').then((m) => m.routes),
      },
    ],
  },
  {
    path: 'users',
    component: UsersList,
    canActivate: [AdminGuard],
    children: [
      {
        path: '',
        loadChildren: () =>
          import('./features/users/users.routes').then((m) => m.routes),
      },
    ],
  },
];
