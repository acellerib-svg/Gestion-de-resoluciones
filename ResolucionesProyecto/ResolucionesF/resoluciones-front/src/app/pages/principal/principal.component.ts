import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardService, DashboardStats } from '../../services/dashboard.service';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-principal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './principal.component.html',
  styleUrls: ['./principal.component.css'],
})
export class PrincipalComponent implements OnInit {
  loading = false;
  error: string | null = null;

  totalResolutions = 0;
  inProgress = 0;
  approved = 0;
  rejected = 0;
  transferred = 0;
  archived = 0;

  constructor(
    private dashboardService: DashboardService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadStats();
  }

  loadStats(): void {
    this.loading = true;
    this.error = null;
    this.cdr.detectChanges();

    this.dashboardService.getStats()
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: (stats: DashboardStats) => {
          this.totalResolutions = stats.totalResolutions;
          this.inProgress = stats.inProgress;
          this.approved = stats.approved;
          this.rejected = stats.rejected;
          this.transferred = stats.transferred;
          this.archived = stats.archived;
          this.cdr.detectChanges();
        },
        error: () => {
          this.error = 'No se pudieron cargar las estadisticas';
          this.cdr.detectChanges();
        }
      });
  }
}
