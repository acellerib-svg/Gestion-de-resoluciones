import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { finalize, timeout } from 'rxjs/operators';

import { ResolutionService } from '../../services/resolution.service';
import { ResolutionResponse } from '../../models/resolution.model';
import { InstanceService } from '../../services/instance';
import { Instance } from '../../models/instance.model';
import { ActService, ActResponse } from '../../services/act.service';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-resolutions',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterLink],
  templateUrl: './resolutions.html',
  styleUrls: ['./resolutions.scss']
})
export class ResolutionsComponent implements OnInit {

  // =====================
  // LISTADO
  // =====================
  resolutions: ResolutionResponse[] = [];
  instances: Instance[] = [];
  acts: ActResponse[] = [];

  page = 0;
  size = 10;
  totalPages = 0;

  // Filters
  filterState: string = '';
  filterInstanceId: number | null = null;

  loading = false;
  saving = false;
  error: string | null = null;

  // =====================
  // CREAR
  // =====================
  showForm = false;
  form!: FormGroup;

  constructor(
    private service: ResolutionService,
    private instanceService: InstanceService,
    private actService: ActService,
    private auth: AuthService,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.form = this.fb.group({
      actId: [null, Validators.required],
      topic: [''],
      currentInstanceId: [null, Validators.required],
      antecedent: ['', Validators.required],
      resolution: ['', Validators.required],
      fundament: [''],
    });

    this.load();
    this.instanceService.getAll().subscribe({
      next: (list) => this.instances = list
    });
    this.actService.listAll().subscribe({
      next: (list) => this.acts = list
    });
  }

  // =====================
  // LISTAR
  // =====================
  load() {
    this.error = null;
    this.loading = true;

    const stateId = this.filterState ? +this.filterState : undefined;
    const instId = this.filterInstanceId ?? undefined;
    this.service.list(this.page, this.size, stateId, instId)
      .pipe(
        timeout(10000),
        finalize(() => this.loading = false)
      )
      .subscribe({
        next: (resp) => {
          this.resolutions = resp.content ?? [];
          this.totalPages = resp.totalPages ?? 0;
        },
        error: (err) => {
          this.error = 'Error cargando resoluciones.';
          console.error(err);
        }
      });
  }

  nextPage() {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.load();
    }
  }

  prevPage() {
    if (this.page > 0) {
      this.page--;
      this.load();
    }
  }

  applyFilter() {
    this.page = 0;
    this.load();
  }

  clearFilters() {
    this.filterState = '';
    this.filterInstanceId = null;
    this.page = 0;
    this.load();
  }

  trackById(_: number, item: ResolutionResponse) {
    return item.id;
  }

  // =====================
  // CREAR
  // =====================
  openCreate() {
    this.error = null;
    this.showForm = true;
    this.form.reset();
  }

  cancel() {
    this.showForm = false;
    this.form.reset();
    this.error = null;
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.error = 'Completa los campos obligatorios.';
      return;
    }

    const user = this.auth.getSession();
    if (!user) {
      this.error = 'Sesion no encontrada. Vuelve a iniciar sesion.';
      return;
    }

    this.error = null;
    this.saving = true;

    const payload = {
      ...this.form.value,
      createdByUserId: user.id
    };

    this.service.create(payload)
      .pipe(
        timeout(10000),
        finalize(() => this.saving = false)
      )
      .subscribe({
        next: () => {
          this.cancel();
          this.page = 0;
          this.load();
        },
        error: (err) => {
          console.error(err);
          this.error = 'No se pudo crear la resolucion. Verifica los datos.';
        }
      });
  }

  // =====================
  // ELIMINAR
  // =====================
  deleteResolution(res: ResolutionResponse) {
    const user = this.auth.getSession();
    if (!user) {
      this.error = 'Sesion no encontrada.';
      return;
    }

    if (!confirm('Eliminar resolucion?')) return;

    this.error = null;
    this.saving = true;

    this.service.delete(res.id, user.id)
      .pipe(finalize(() => this.saving = false))
      .subscribe({
        next: () => this.load(),
        error: (err) => {
          console.error(err);
          this.error = 'No se pudo eliminar la resolucion.';
        }
      });
  }
}
