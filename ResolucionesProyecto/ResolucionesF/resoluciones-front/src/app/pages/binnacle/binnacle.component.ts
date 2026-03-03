import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs/operators';
import { BinnacleService, BinnacleEntry, PageResponse } from '../../services/binnacle.service';

@Component({
  selector: 'app-binnacle',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './binnacle.component.html',
  styleUrls: ['./binnacle.component.css']
})
export class BinnacleComponent implements OnInit {
  loading = false;
  error: string | null = null;

  entries: BinnacleEntry[] = [];
  page = 0;
  size = 15;
  totalPages = 0;
  totalElements = 0;

  // filtros
  filterResolutionId: number | null = null;
  filterUserId: number | null = null;

  constructor(
    private service: BinnacleService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.error = null;
    this.cdr.detectChanges();

    this.service
      .list(
        this.page,
        this.size,
        this.filterResolutionId ?? undefined,
        this.filterUserId ?? undefined
      )
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: (res: PageResponse<BinnacleEntry>) => {
          this.entries = res.content ?? [];
          this.totalPages = res.totalPages;
          this.totalElements = res.totalElements;
          this.cdr.detectChanges();
        },
        error: () => {
          this.error = 'No se pudieron cargar los registros de bitacora';
          this.cdr.detectChanges();
        }
      });
  }

  applyFilter(): void {
    this.page = 0;
    this.load();
  }

  clearFilter(): void {
    this.filterResolutionId = null;
    this.filterUserId = null;
    this.page = 0;
    this.load();
  }

  prevPage(): void {
    if (this.page > 0) {
      this.page--;
      this.load();
    }
  }

  nextPage(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.load();
    }
  }

  formatDate(iso?: string): string {
    if (!iso) return '';
    const d = new Date(iso);
    return d.toLocaleString();
  }

  trackById(_index: number, item: BinnacleEntry): number {
    return item.id;
  }
}
