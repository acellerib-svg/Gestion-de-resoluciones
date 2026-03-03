import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, NavigationEnd } from '@angular/router';
import { filter, finalize } from 'rxjs/operators';
import {
  HierarchicalLevelService,
  HierarchicalLevel,
  HierarchicalLevelRequest
} from '../../services/hierarchical-level.service';

@Component({
  selector: 'app-hierarchical-levels',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './hierarchical-levels.component.html',
  styleUrls: ['./hierarchical-levels.component.css']
})
export class HierarchicalLevelsComponent implements OnInit {
  loading = false;
  error: string | null = null;

  levels: HierarchicalLevel[] = [];

  // formulario
  formOpen = false;
  editingId: number | null = null;
  name = '';
  levelOrder: number | null = null;
  description = '';
  active = true;

  constructor(
    private service: HierarchicalLevelService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.load();

    this.router.events
      .pipe(filter(e => e instanceof NavigationEnd))
      .subscribe(() => {
        if (this.router.url.startsWith('/hierarchical-levels')) {
          this.load();
        }
      });
  }

  load(): void {
    this.loading = true;
    this.error = null;
    this.cdr.detectChanges();

    this.service.list()
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: (res) => {
          this.levels = Array.isArray(res) ? res : [];
          this.cdr.detectChanges();
        },
        error: () => {
          this.error = 'No se pudieron cargar los niveles jerárquicos';
          this.cdr.detectChanges();
        }
      });
  }

  trackById(_index: number, item: HierarchicalLevel): number {
    return item.id;
  }

  openForm(): void {
    this.formOpen = true;
    this.editingId = null;
    this.name = '';
    this.levelOrder = null;
    this.description = '';
    this.active = true;
  }

  closeForm(): void {
    this.formOpen = false;
    this.editingId = null;
    this.name = '';
    this.levelOrder = null;
    this.description = '';
    this.active = true;
    this.error = null;
  }

  editLevel(level: HierarchicalLevel): void {
    this.formOpen = true;
    this.editingId = level.id;
    this.name = level.name;
    this.levelOrder = level.order;
    this.description = level.description ?? '';
    this.active = level.active;
  }

  save(): void {
    if (this.loading) return;

    const trimmedName = (this.name ?? '').trim();
    const order = Number(this.levelOrder);

    if (!trimmedName || !order) {
      this.error = 'Completa nombre y orden';
      this.cdr.detectChanges();
      return;
    }

    const req: HierarchicalLevelRequest = {
      name: trimmedName,
      levelOrder: order,
      description: this.description?.trim() || undefined,
      active: !!this.active,
    };

    this.loading = true;
    this.error = null;
    this.cdr.detectChanges();

    const action$ = this.editingId
      ? this.service.update(this.editingId, req)
      : this.service.create(req);

    action$
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: () => {
          this.closeForm();
          this.load();
        },
        error: () => {
          this.error = this.editingId
            ? 'No se pudo actualizar el nivel jerárquico'
            : 'No se pudo crear el nivel jerárquico';
          this.cdr.detectChanges();
        }
      });
  }

  deleteLevel(id: number): void {
    if (this.loading) return;

    this.loading = true;
    this.error = null;
    this.cdr.detectChanges();

    this.service.delete(id)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: () => {
          this.load();
        },
        error: () => {
          this.error = 'No se pudo eliminar el nivel jerárquico';
          this.cdr.detectChanges();
        }
      });
  }
}
