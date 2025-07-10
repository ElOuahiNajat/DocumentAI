import {Routes} from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/users-list/users-list').then(m => m.UsersList),
    title: 'Users list'
  }
];
