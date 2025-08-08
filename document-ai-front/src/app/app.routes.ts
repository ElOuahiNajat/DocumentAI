import {Routes} from '@angular/router';
import {DocumentPage} from './features/documents/pages/document-page/document-page';
import { UsersList } from './features/users/pages/users-list/users-list';
export const routes: Routes = [
  {
    path: "documents",
    component: DocumentPage,
    children: [
      {
        path: "",
        loadChildren: () => import("./features/documents/documents.routes").then((m) => m.routes),
      },
    ],
  },
  {
    path: "users",
    component: UsersList,
    children: [
      {
        path: "",
        loadChildren: () => import("./features/users/users.routes").then((m) => m.routes),
      },
    ],
  },
];
