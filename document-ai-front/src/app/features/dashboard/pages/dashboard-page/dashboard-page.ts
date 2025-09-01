import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardService, DashboardStats } from '../../services/dashboard.service';

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="p-6 max-w-7xl mx-auto">
      <h1 class="text-3xl font-bold text-gray-800 mb-2">Tableau de bord</h1>
      <p class="text-gray-500 mb-8">Vue d'ensemble de votre activité</p>

      <div *ngIf="stats; else loading" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5 mb-8">
        <!-- Carte principale - Documents totaux -->
        <div class="bg-gradient-to-r from-blue-500 to-blue-600 text-white p-5 rounded-xl shadow-lg">
          <div class="flex justify-between items-start">
            <div>
              <h2 class="font-medium text-blue-100 mb-1">Documents</h2>
              <p class="text-3xl font-bold">{{ stats.totalDocuments }}</p>
            </div>
            <div class="bg-white/20 p-2 rounded-lg">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
            </div>
          </div>
          <div class="mt-4 flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
            </svg>
            <span class="text-xs font-medium">Total des documents</span>
          </div>
        </div>

        <!-- Carte principale - Utilisateurs -->
        <div class="bg-white p-5 rounded-xl shadow-sm border border-gray-100">
          <div class="flex justify-between items-start">
            <div>
              <h2 class="font-medium text-gray-500 mb-1">Utilisateurs</h2>
              <p class="text-3xl font-bold text-gray-800">{{ stats.totalUsers }}</p>
            </div>
            <div class="bg-blue-100 text-blue-600 p-2 rounded-lg">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
              </svg>
            </div>
          </div>
          <div class="mt-4 flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1 text-green-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
            </svg>
            <span class="text-xs font-medium text-gray-500">Utilisateurs actifs</span>
          </div>
        </div>

        <!-- Carte principale - Documents avec feedback -->
        <div class="bg-white p-5 rounded-xl shadow-sm border border-gray-100">
          <div class="flex justify-between items-start">
            <div>
              <h2 class="font-medium text-gray-500 mb-1">Avec feedback</h2>
              <p class="text-3xl font-bold text-gray-800">{{ stats.documentsWithFeedback }}</p>
            </div>
            <div class="bg-green-100 text-green-600 p-2 rounded-lg">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 10h4.764a2 2 0 011.789 2.894l-3.5 7A2 2 0 0115.263 21h-4.017c-.163 0-.326-.02-.485-.06L7 20m7-10V5a2 2 0 00-2-2h-.095c-.5 0-.905.405-.905.905a3.61 3.61 0 01-.608 2.006L7 11v9m7-10h-2M7 20H5a2 2 0 01-2-2v-6a2 2 0 012-2h2.5" />
              </svg>
            </div>
          </div>
          <div class="mt-4">
            <div class="w-full bg-gray-200 rounded-full h-2">
              <div class="bg-green-600 h-2 rounded-full" [style.width]="(stats.documentsWithFeedback / stats.totalDocuments * 100) + '%'"></div>
            </div>
            <p class="text-xs text-gray-500 mt-2">{{ (stats.documentsWithFeedback / stats.totalDocuments * 100) | number:'1.0-0' }}% des documents</p>
          </div>
        </div>

        <!-- Carte principale - Documents récents -->
        <div class="bg-white p-5 rounded-xl shadow-sm border border-gray-100">
          <div class="flex justify-between items-start">
            <div>
              <h2 class="font-medium text-gray-500 mb-1">Documents récents</h2>
              <p class="text-3xl font-bold text-gray-800">{{ stats.documentsRecent }}</p>
            </div>
            <div class="bg-amber-100 text-amber-600 p-2 rounded-lg">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
          </div>
          <div class="mt-4 flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1 text-green-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
            </svg>
            <span class="text-xs font-medium text-gray-500">Ajoutés récemment</span>
          </div>
        </div>
      </div>

      <div *ngIf="stats" class="grid grid-cols-1 md:grid-cols-3 gap-5">
        <!-- Type de documents -->
        <div class="md:col-span-2 bg-white p-6 rounded-xl shadow-sm border border-gray-100">
          <h2 class="text-lg font-semibold text-gray-800 mb-4">Types de documents</h2>
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <!-- PDF -->
            <div class="flex items-center p-4 bg-gray-50 rounded-lg">
              <div class="bg-red-100 p-3 rounded-lg mr-4">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-red-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21h10a2 2 0 002-2V9.414a1 1 0 00-.293-.707l-5.414-5.414A1 1 0 0012.586 3H7a2 2 0 00-2 2v14a2 2 0 002 2z" />
                </svg>
              </div>
              <div>
                <h3 class="font-medium text-gray-500">PDF</h3>
                <p class="text-2xl font-bold text-gray-800">{{ stats.documentsPdf }}</p>
                <p class="text-xs text-gray-500">{{ (stats.documentsPdf / stats.totalDocuments * 100) | number:'1.0-0' }}% du total</p>
              </div>
            </div>

            <!-- Word -->
            <div class="flex items-center p-4 bg-gray-50 rounded-lg">
              <div class="bg-blue-100 p-3 rounded-lg mr-4">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21h10a2 2 0 002-2V9.414a1 1 0 00-.293-.707l-5.414-5.414A1 1 0 0012.586 3H7a2 2 0 00-2 2v14a2 2 0 002 2z" />
                </svg>
              </div>
              <div>
                <h3 class="font-medium text-gray-500">Word</h3>
                <p class="text-2xl font-bold text-gray-800">{{ stats.documentsWord }}</p>
                <p class="text-xs text-gray-500">{{ (stats.documentsWord / stats.totalDocuments * 100) | number:'1.0-0' }}% du total</p>
              </div>
            </div>

            <!-- Excel -->
            <div class="flex items-center p-4 bg-gray-50 rounded-lg">
              <div class="bg-green-100 p-3 rounded-lg mr-4">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21h10a2 2 0 002-2V9.414a1 1 0 00-.293-.707l-5.414-5.414A1 1 0 0012.586 3H7a2 2 0 00-2 2v14a2 2 0 002 2z" />
                </svg>
              </div>
              <div>
                <h3 class="font-medium text-gray-500">Excel</h3>
                <p class="text-2xl font-bold text-gray-800">{{ stats.documentsExcel }}</p>
                <p class="text-xs text-gray-500">{{ (stats.documentsExcel / stats.totalDocuments * 100) | number:'1.0-0' }}% du total</p>
              </div>
            </div>

            <!-- Sans feedback -->
            <div class="flex items-center p-4 bg-gray-50 rounded-lg">
              <div class="bg-gray-200 p-3 rounded-lg mr-4">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-gray-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                </svg>
              </div>
              <div>
                <h3 class="font-medium text-gray-500">Sans feedback</h3>
                <p class="text-2xl font-bold text-gray-800">{{ stats.documentsWithoutFeedback }}</p>
                <p class="text-xs text-gray-500">{{ (stats.documentsWithoutFeedback / stats.totalDocuments * 100) | number:'1.0-0' }}% du total</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Statistiques de feedback -->
        <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
          <h2 class="text-lg font-semibold text-gray-800 mb-4">Répartition des feedbacks</h2>
          <div class="flex justify-center mb-4">
            <div class="relative">
              <svg class="w-40 h-40 transform -rotate-90">
                <circle cx="80" cy="80" r="70" stroke="#e5e7eb" stroke-width="10" fill="transparent" />
                <circle cx="80" cy="80" r="70" stroke="#3b82f6" stroke-width="10" fill="transparent"
                        [attr.stroke-dasharray]="439.8"
                        [attr.stroke-dashoffset]="439.8 - (stats.documentsWithFeedback / stats.totalDocuments * 439.8)" />
              </svg>
              <div class="absolute inset-0 flex flex-col items-center justify-center">
                <span class="text-3xl font-bold text-gray-800">{{ (stats.documentsWithFeedback / stats.totalDocuments * 100) | number:'1.0-0' }}%</span>
                <span class="text-sm text-gray-500">avec feedback</span>
              </div>
            </div>
          </div>
          <div class="space-y-3">
            <div class="flex justify-between items-center">
              <div class="flex items-center">
                <div class="w-3 h-3 rounded-full bg-blue-500 mr-2"></div>
                <span class="text-sm">Avec feedback</span>
              </div>
              <span class="text-sm font-semibold">{{ stats.documentsWithFeedback }}</span>
            </div>
            <div class="flex justify-between items-center">
              <div class="flex items-center">
                <div class="w-3 h-3 rounded-full bg-gray-300 mr-2"></div>
                <span class="text-sm">Sans feedback</span>
              </div>
              <span class="text-sm font-semibold">{{ stats.documentsWithoutFeedback }}</span>
            </div>
          </div>
        </div>
      </div>

      <ng-template #loading>
        <div class="flex flex-col items-center justify-center h-64">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mb-4"></div>
          <p class="text-gray-500">Chargement des statistiques...</p>
        </div>
      </ng-template>
    </div>
  `,
  styles: [`
    .stat-card {
      transition: transform 0.2s ease, box-shadow 0.2s ease;
    }
    .stat-card:hover {
      transform: translateY(-2px);
      box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
    }
  `]
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
