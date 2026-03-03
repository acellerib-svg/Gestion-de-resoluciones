import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { PermissionService, PermissionCreateRequest, PermissionResponse } from '../../services/permission';

@Component({
  selector: 'app-permission',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './permission.html',
  styleUrls: ['./permission.scss']
})
export class PermissionComponent implements OnInit {

  permissions: PermissionResponse[] = [];
  filtered: PermissionResponse[] = [];

  form: FormGroup;
  showForm = false;
  isEdit = false;
  editingId?: number;

  loading = false;
  saving = false;
  error: string | null = null;

  statusFilter: 'ALL' | 'ACTIVE' | 'INACTIVE' = 'ALL';
  search = '';

  constructor(
    private fb: FormBuilder,
    private service: PermissionService,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      code: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(80)]],
      name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(120)]],
      module: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      description: [''],
      active: [true, [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.error = null;

    this.service.list().subscribe({
      next: (data) => {
        this.permissions = data ?? [];
        this.applyFilters();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.permissions = [];
        this.filtered = [];
        this.loading = false;
        this.error = 'No se pudieron cargar los permisos.';
        this.cdr.detectChanges();
      }
    });
  }

  openCreate(): void {
    this.showForm = true;
    this.isEdit = false;
    this.editingId = undefined;
    this.error = null;

    this.form.reset({
      code: '',
      name: '',
      module: '',
      description: '',
      active: true
    });
  }

  openEdit(p: PermissionResponse): void {
    this.showForm = true;
    this.isEdit = true;
    this.editingId = p.id as number;

    this.form.patchValue({
      code: p.code,
      name: p.name,
      module: p.module,
      description: p.description ?? '',
      active: !!p.active
    });
  }

  cancel(): void {
    this.showForm = false;
    this.form.reset({ active: true });
    this.error = null;
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const req: PermissionCreateRequest = {
      code: (this.form.value.code ?? '').trim(),
      name: (this.form.value.name ?? '').trim(),
      module: (this.form.value.module ?? '').trim(),
      description: (this.form.value.description ?? '').trim() || undefined,
      active: !!this.form.value.active
    };

    this.saving = true;
    this.error = null;

    const call$ = this.isEdit && this.editingId
      ? this.service.update(this.editingId, req)
      : this.service.create(req);

    call$.subscribe({
      next: () => {
        this.saving = false;
        this.showForm = false;
        this.form.reset({ active: true });
        this.load();
      },
      error: () => {
        this.saving = false;
        this.error = this.isEdit
          ? 'No se pudo actualizar el permiso.'
          : 'No se pudo crear el permiso.';
        this.cdr.detectChanges();
      }
    });
  }

  toggle(p: PermissionResponse): void {
    if (!p.id) return;
    const action = p.active ? 'desactivar' : 'activar';
    if (!confirm(`¿Deseas ${action} el permiso "${p.code}"?`)) return;

    this.saving = true;
    this.service.toggle(p.id as number).subscribe({
      next: () => {
        this.saving = false;
        this.load();
      },
      error: () => {
        this.saving = false;
        alert('No se pudo cambiar el estado del permiso');
      }
    });
  }

  applyFilters(): void {
    const q = (this.search ?? '').trim().toLowerCase();

    let list = [...this.permissions];

    if (this.statusFilter === 'ACTIVE') list = list.filter(p => !!p.active);
    if (this.statusFilter === 'INACTIVE') list = list.filter(p => !p.active);

    if (q) {
      list = list.filter(p =>
        (p.code ?? '').toLowerCase().includes(q) ||
        (p.name ?? '').toLowerCase().includes(q) ||
        (p.module ?? '').toLowerCase().includes(q)
      );
    }

    this.filtered = list;
  }

  trackById(index: number, item: PermissionResponse): number {
    return item.id as number;
  }
}
