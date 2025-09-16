import type { Routes } from "@angular/router";

// @ts-ignore
export const chatbotRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./chat.component').then(m => m.ChatComponent),
    title: 'Chatbot',
  }
];
