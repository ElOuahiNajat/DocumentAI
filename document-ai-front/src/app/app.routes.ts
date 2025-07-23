import {Routes} from '@angular/router';
import {DocumentPage} from './features/documents/pages/document-page/document-page';
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
];
