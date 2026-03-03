import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs/operators';
import { FacultyService, Faculty, FacultyRequest } from '../../services/faculty.service';

@Component({
  selector: 'app-faculty',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './faculty.component.html',
  styleUrls: ['./faculty.component.css']
})
export class FacultyComponent implements OnInit {
  loading = false;
  error: string | null = null;

  faculties: Faculty[] = [];

  // formulario
  formOpen = false;
  editingId: number | null = null;
  name = '';
  description = '';
  active = true;

  constructor(
    private service: FacultyService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.load();
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
          this.faculties = Array.isArray(res) ? res : [];
          this.cdr.detectChanges();
        },
        error: () => {
          this.error = 'No se pudieron cargar las facultades';
          this.cdr.detectChanges();
        }
      });
  }

  trackById(_index: number, item: Faculty): number {
    return item.id;
  }

  openForm(): void {
    this.formOpen = true;
    this.editingId = null;
    this.name = '';
    this.description = '';
    this.active = true;
  }

  closeForm(): void {
    this.formOpen = false;
    this.editingId = null;
    this.name = '';
    this.description = '';
    this.active = true;
    this.error = null;
  }

  editFaculty(faculty: Faculty): void {
    this.formOpen = true;
    this.editingId = faculty.id;
    this.name = faculty.name;
    this.description = faculty.description ?? '';
    this.active = faculty.active;
  }

  save(): void {
    if (this.loading) return;

    const trimmedName = (this.name ?? '').trim();

    if (!trimmedName) {
      this.error = 'El nombre es obligatorio';
      this.cdr.detectChanges();
      return;
    }

    const req: FacultyRequest = {
      name: trimmedName,
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
            ? 'No se pudo actualizar la facultad'
            : 'No se pudo crear la facultad';
          this.cdr.detectChanges();
        }
      });
  }

  deleteFaculty(id: number): void {
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
          this.error = 'No se pudo eliminar la facultad';
          this.cdr.detectChanges();
        }
      });
  }
}
