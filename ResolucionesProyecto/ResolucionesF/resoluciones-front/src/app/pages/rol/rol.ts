    import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RolService } from '../../services/rol';
import { Rol } from '../../models/rol.model';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-rol',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    RouterLink
  ],
  templateUrl: './rol.html',
  styleUrls: ['./rol.scss']
})
export class RolComponent implements OnInit {

  roles: Rol[] = [];
  filteredRoles: Rol[] = [];
  statusFilter: 'ALL' | 'ACTIVE' | 'INACTIVE' = 'ALL';

  form: FormGroup;
  showForm = false;
  isEdit = false;
  editingId?: number;

  loading = false;
  saving = false;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private rolService: RolService,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      description: [''],
      active: [true]
    });
  }

  ngOnInit(): void {
    this.loadRoles();
  }

  // ======================
  // LOAD
  // ======================
  loadRoles(): void {
    this.loading = true;
    this.error = null;

    this.rolService.getAll().subscribe({
      next: (data) => {
        this.roles = data;
        this.applyFilter();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'No se pudieron cargar los roles.';
        this.roles = [];
        this.filteredRoles = [];
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  // ======================
  // FILTER
  // ======================
  applyFilter(): void {
    switch (this.statusFilter) {
      case 'ACTIVE':
        this.filteredRoles = this.roles.filter(r => r.active);
        break;
      case 'INACTIVE':
        this.filteredRoles = this.roles.filter(r => !r.active);
        break;
      default:
        this.filteredRoles = [...this.roles];
    }
  }

  // ======================
  // CREATE
  // ======================
  openCreate(): void {
    this.showForm = true;
    this.isEdit = false;
    this.editingId = undefined;
    this.error = null;

    this.form.reset({
      name: '',
      description: '',
      active: true
    });
  }

  // ======================
  // EDIT
  // ======================
  openEdit(rol: Rol): void {
    this.showForm = true;
    this.isEdit = true;
    this.editingId = rol.id;

    this.form.patchValue({
      name: rol.name,
      description: rol.description ?? ''
    });
  }

  // ======================
  // SAVE (CREATE / UPDATE)
  // ======================
  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving = true;
    this.error = null;

    const data = this.form.value;

    const request$ = this.isEdit && this.editingId
      ? this.rolService.update(this.editingId, data)
      : this.rolService.create(data);

    request$.subscribe({
      next: () => this.afterSave(),
      error: () => {
        this.error = this.isEdit
          ? 'No se pudo actualizar el rol.'
          : 'No se pudo crear el rol.';
        this.saving = false;
        this.cdr.detectChanges();
      }
    });
  }

  private afterSave(): void {
    this.saving = false;
    this.showForm = false;
    this.form.reset();
    this.error = null;
    this.loadRoles();
  }

  // ======================
  // ACTIVATE / DEACTIVATE (SP)
  // ======================
  toggleRolStatus(rol: Rol): void {
    const action = rol.active ? 'desactivar' : 'activar';

    if (!confirm(`¿Deseas ${action} el rol "${rol.name}"?`)) {
      return;
    }

    this.saving = true;

    this.rolService.toggleStatus(rol.id!).subscribe({
      next: () => {
        this.loadRoles();
        this.saving = false;
      },
      error: () => {
        alert('No se pudo cambiar el estado del rol');
        this.saving = false;
      }
    });
  }


  // ======================
  // UTILS
  // ======================
  cancel(): void {
    this.showForm = false;
    this.form.reset();
    this.error = null;
  }

  trackById(index: number, item: Rol): number {
    return item.id!;
  }
}
