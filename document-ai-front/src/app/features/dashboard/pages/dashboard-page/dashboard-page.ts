import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardService, DashboardStats } from '../../services/dashboard.service';

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="p-4 max-w-5xl mx-auto">
      <h1 class="text-2xl font-bold mb-6 text-center">Tableau de bord</h1>

      <div *ngIf="stats; else loading" class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
        <div class="p-4 rounded-lg shadow-sm bg-gray-50 text-center">
          <h2 class="font-medium mb-2">Documents</h2>
          <p class="text-2xl font-semibold">{{ stats.totalDocuments }}</p>
        </div>

        <div class="p-4 rounded-lg shadow-sm bg-gray-50 text-center">
          <h2 class="font-medium mb-2">Utilisateurs</h2>
          <p class="text-2xl font-semibold">{{ stats.totalUsers }}</p>
        </div>

        <div class="p-4 rounded-lg shadow-sm bg-gray-50 text-center">
          <h2 class="font-medium mb-2">PDF</h2>
          <p class="text-2xl font-semibold">{{ stats.documentsPdf }}</p>
        </div>

        <div class="p-4 rounded-lg shadow-sm bg-gray-50 text-center">
          <h2 class="font-medium mb-2">Word</h2>
          <p class="text-2xl font-semibold">{{ stats.documentsWord }}</p>
        </div>

        <div class="p-4 rounded-lg shadow-sm bg-gray-50 text-center">
          <h2 class="font-medium mb-2">Excel</h2>
          <p class="text-2xl font-semibold">{{ stats.documentsExcel }}</p>
        </div>

        <div class="p-4 rounded-lg shadow-sm bg-gray-50 text-center">
          <h2 class="font-medium mb-2">Documents récents</h2>
          <p class="text-2xl font-semibold">{{ stats.documentsRecent }}</p>
        </div>

        <div class="p-4 rounded-lg shadow-sm bg-gray-50 text-center">
          <h2 class="font-medium mb-2">Avec feedback</h2>
          <p class="text-2xl font-semibold">{{ stats.documentsWithFeedback }}</p>
        </div>

        <div class="p-4 rounded-lg shadow-sm bg-gray-50 text-center">
          <h2 class="font-medium mb-2">Sans feedback</h2>
          <p class="text-2xl font-semibold">{{ stats.documentsWithoutFeedback }}</p>
        </div>
      </div>

      <ng-template #loading>
        <p class="text-center text-gray-500 mt-6">Chargement des statistiques...</p>
      </ng-template>
    </div>
  `,
})
export class DashboardPage implements OnInit {
  stats!: DashboardStats;

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.dashboardService.getStats().subscribe({
      next: (data) => (this.stats = data),
      error: (err) => console.error('Erreur lors du chargement du dashboard', err)
    });
  }
}

