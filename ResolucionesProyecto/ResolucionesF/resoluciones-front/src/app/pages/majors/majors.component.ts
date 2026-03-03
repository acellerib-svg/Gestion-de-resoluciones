import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs/operators';
import { MajorsService, Major, MajorRequest } from '../../services/majors.service';
import { FacultyService, Faculty } from '../../services/faculty.service';

@Component({
  selector: 'app-majors',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './majors.component.html',
  styleUrls: ['./majors.component.css']
})
export class MajorsComponent implements OnInit {
  loading = false;
  error: string | null = null;

  majors: Major[] = [];
  faculties: Faculty[] = [];

  // formulario
  formOpen = false;
  editingId: number | null = null;
  name = '';
  description = '';
  facultyId: number | null = null;
  active = true;

  constructor(
    private service: MajorsService,
    private facultyService: FacultyService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadFaculties();
    this.load();
  }

  loadFaculties(): void {
    this.facultyService.list().subscribe({
      next: (res) => {
        this.faculties = Array.isArray(res) ? res : [];
        this.cdr.detectChanges();
      },
      error: () => {
        this.cdr.detectChanges();
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
          this.majors = Array.isArray(res) ? res : [];
          this.cdr.detectChanges();
        },
        error: () => {
          this.error = 'No se pudieron cargar las carreras';
          this.cdr.detectChanges();
        }
      });
  }

  trackById(_index: number, item: Major): number {
    return item.id;
  }

  openForm(): void {
    this.formOpen = true;
    this.editingId = null;
    this.name = '';
    this.description = '';
    this.facultyId = null;
    this.active = true;
  }

  closeForm(): void {
    this.formOpen = false;
    this.editingId = null;
    this.name = '';
    this.description = '';
    this.facultyId = null;
    this.active = true;
    this.error = null;
  }

  editMajor(major: Major): void {
    this.formOpen = true;
    this.editingId = major.id;
    this.name = major.name;
    this.description = major.description ?? '';
    this.facultyId = major.faculty?.id ?? null;
    this.active = major.active;
  }

  save(): void {
    if (this.loading) return;

    const trimmedName = (this.name ?? '').trim();

    if (!trimmedName) {
      this.error = 'El nombre es obligatorio';
      this.cdr.detectChanges();
      return;
    }

    if (!this.facultyId) {
      this.error = 'Debe seleccionar una facultad';
      this.cdr.detectChanges();
      return;
    }

    const req: MajorRequest = {
      name: trimmedName,
      description: this.description?.trim() || undefined,
      active: !!this.active,
      faculty: { id: this.facultyId },
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
            ? 'No se pudo actualizar la carrera'
            : 'No se pudo crear la carrera';
          this.cdr.detectChanges();
        }
      });
  }

  deleteMajor(id: number): void {
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
          this.error = 'No se pudo eliminar la carrera';
          this.cdr.detectChanges();
        }
      });
  }
}
