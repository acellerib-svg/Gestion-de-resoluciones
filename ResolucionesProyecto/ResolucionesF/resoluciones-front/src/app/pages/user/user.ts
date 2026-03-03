import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {UserService} from '../../services/user';
import {  UserCreateRequest, UserUpdateRequest } from '../../models/user.dto';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './user.html',
  styleUrls: ['./user.scss']
})
export class UserComponent implements OnInit {

  users: User[] = [];
  filtered: User[] = [];

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
    private service: UserService,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      user: ['', [Validators.required, Validators.minLength(2)]],
      password: ['', [Validators.required, Validators.minLength(4)]], // ✅ solo requerido en CREATE
      names: ['', [Validators.required, Validators.minLength(2)]],
      surnames: ['', [Validators.required, Validators.minLength(2)]],
      phone: [''],
      email: ['', [Validators.required, Validators.email]],
      state: [true, [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.error = null;

    this.service.getAll().subscribe({
      next: (data) => {
        this.users = data ?? [];
        this.applyFilters();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.users = [];
        this.filtered = [];
        this.loading = false;
        this.error = 'No se pudieron cargar los usuarios.';
        this.cdr.detectChanges();
      }
    });
  }

  openCreate(): void {
    this.showForm = true;
    this.isEdit = false;
    this.editingId = undefined;
    this.error = null;

    // ✅ En create el password es obligatorio
    this.form.reset({
      user: '',
      password: '',
      names: '',
      surnames: '',
      phone: '',
      email: '',
      state: true
    });

    this.form.get('password')?.setValidators([Validators.required, Validators.minLength(4)]);
    this.form.get('password')?.updateValueAndValidity();
  }

  openEdit(u: User): void {
    this.showForm = true;
    this.isEdit = true;
    this.editingId = Number(u.id);

    // ✅ En edit NO pedimos password (se deja aparte)
    this.form.patchValue({
      user: u.user,
      password: '', // se limpia
      names: u.names,
      surnames: u.surnames,
      phone: u.phone ?? '',
      email: u.email,
      state: !!u.state
    });

    this.form.get('password')?.clearValidators();
    this.form.get('password')?.updateValueAndValidity();
  }

  cancel(): void {
    this.showForm = false;
    this.form.reset({ state: true });
    this.error = null;
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving = true;
    this.error = null;

    const v = this.form.value;

    if (!this.isEdit) {
      const req: UserCreateRequest = {
        user: (v.user ?? '').trim(),
        password: (v.password ?? '').trim(),
        names: (v.names ?? '').trim(),
        surnames: (v.surnames ?? '').trim(),
        phone: (v.phone ?? '').trim() || undefined,
        email: (v.email ?? '').trim(),
        state: !!v.state
      };

      this.service.create(req).subscribe({
        next: () => {
          this.saving = false;
          this.showForm = false;
          this.form.reset({ state: true });
          this.load();
        },
        error: () => {
          this.saving = false;
          this.error = 'No se pudo crear el usuario.';
          this.cdr.detectChanges();
        }
      });

      return;
    }

    // UPDATE (sin password)
    const req: UserUpdateRequest = {
      id: this.editingId!,
      user: (v.user ?? '').trim(),
      names: (v.names ?? '').trim(),
      surnames: (v.surnames ?? '').trim(),
      phone: (v.phone ?? '').trim() || undefined,
      email: (v.email ?? '').trim(),
      state: !!v.state
    };

    this.service.update(this.editingId!, req).subscribe({
      next: () => {
        this.saving = false;
        this.showForm = false;
        this.form.reset({ state: true });
        this.load();
      },
      error: () => {
        this.saving = false;
        this.error = 'No se pudo actualizar el usuario.';
        this.cdr.detectChanges();
      }
    });
  }

  toggleState(u: User): void {
    if (!u.id) return;

    const action = u.state ? 'desactivar' : 'activar';
    if (!confirm(`¿Deseas ${action} al usuario "${u.user}"?`)) return;

    // Reutilizamos update para cambiar state
    const req: UserUpdateRequest = {
      id: Number(u.id),
      user: u.user,
      names: u.names,
      surnames: u.surnames,
      phone: u.phone ?? undefined,
      email: u.email,
      state: !u.state
    };

    this.saving = true;
    this.service.update(Number(u.id), req).subscribe({
      next: () => {
        this.saving = false;
        this.load();
      },
      error: () => {
        this.saving = false;
        alert('No se pudo cambiar el estado del usuario');
      }
    });
  }

  // (Opcional) eliminar
  delete(u: User): void {
    if (!u.id) return;
    if (!confirm(`¿Eliminar usuario "${u.user}"?`)) return;

    this.saving = true;
    this.service.delete(Number(u.id)).subscribe({
      next: () => {
        this.saving = false;
        this.load();
      },
      error: () => {
        this.saving = false;
        alert('No se pudo eliminar el usuario');
      }
    });
  }

  applyFilters(): void {
    const q = (this.search ?? '').trim().toLowerCase();
    let list = [...this.users];

    if (this.statusFilter === 'ACTIVE') list = list.filter(u => !!u.state);
    if (this.statusFilter === 'INACTIVE') list = list.filter(u => !u.state);

    if (q) {
      list = list.filter(u =>
        (u.user ?? '').toLowerCase().includes(q) ||
        (u.names ?? '').toLowerCase().includes(q) ||
        (u.surnames ?? '').toLowerCase().includes(q) ||
        (u.email ?? '').toLowerCase().includes(q)
      );
    }

    this.filtered = list;
    this.cdr.detectChanges();
  }

  trackById(index: number, item: User): number {
    return Number(item.id);
  }
}
