import type { Routes } from "@angular/router"

// @ts-ignore
export const routes: Routes = [
  {
    path: "",
    loadComponent: () => import("./pages/document-page/document-page").then((m) => m.DocumentPage),
    title: "Document List",
  }
  
]
