import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { InstanceService } from '../../services/instance';
import { Instance } from '../../models/instance.model';
import { HierarchicalLevelService, HierarchicalLevel } from '../../services/hierarchical-level.service';
import { MajorsService, Major } from '../../services/majors.service';

@Component({
  selector: 'app-instance',
  templateUrl: './instance.component.html',
  styleUrls: ['./instance.component.scss'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule]
})
export class InstanceComponent implements OnInit {

  instances: Instance[] = [];
  levels: HierarchicalLevel[] = [];
  majors: Major[] = [];
  showForm = false;
  isEdit = false;
  editingId?: number;
  form: FormGroup;
  error: string | null = null;
  loading = false;
  saving = false;

  constructor(
    private fb: FormBuilder,
    private instanceService: InstanceService,
    private levelService: HierarchicalLevelService,
    private majorsService: MajorsService,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      description: [''],
      levelId: [null, Validators.required],
      majorId: [null],
      instanceFatherId: [null],
      active: [true]
    });
  }

  ngOnInit(): void {
    this.loadInstances();
    this.loadLevels();
    this.loadMajors();
  }

  loadLevels(): void {
    this.levelService.list().subscribe({
      next: (data) => { this.levels = data; this.cdr.detectChanges(); },
      error: () => {}
    });
  }

  loadMajors(): void {
    this.majorsService.list().subscribe({
      next: (data) => { this.majors = data; this.cdr.detectChanges(); },
      error: () => {}
    });
  }

  loadInstances(): void {
    this.loading = true;
    this.error = null;

    this.instanceService.getAll().subscribe({
      next: (data) => {
        this.instances = data.map(inst => ({
          ...inst,
          levelId: inst.levelId ?? undefined,
          majorId: inst.majorId ?? undefined,
          instanceFatherId: inst.instanceFatherId ?? undefined
        }));
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'No se pudieron cargar las instancias.';
        this.loading = false;
        this.instances = [];
        this.cdr.detectChanges();
      }
    });
  }

  openCreate(): void {
    this.showForm = true;
    this.isEdit = false;
    this.editingId = undefined;
    this.form.reset({
      name: '',
      description: '',
      levelId: null,
      majorId: null,
      instanceFatherId: null,
      active: true
    });
    this.error = null;
  }

  openEdit(instance: Instance): void {
    this.showForm = true;
    this.isEdit = true;
    this.editingId = instance.id;

    this.form.patchValue({
      name: instance.name,
      description: instance.description || '',
      levelId: instance.levelId,
      majorId: instance.majorId,
      instanceFatherId: instance.instanceFatherId,
      active: instance.active
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving = true;
    this.error = null;
    const raw = this.form.value;

    const data = {
      ...raw,
      levelId: raw.levelId ? +raw.levelId : null,
      majorId: raw.majorId ? +raw.majorId : null,
      instanceFatherId: raw.instanceFatherId ? +raw.instanceFatherId : null
    };

    const action = this.isEdit && this.editingId
      ? this.instanceService.update(this.editingId, data)
      : this.instanceService.create(data);

    action.subscribe({
      next: () => this.afterSave(),
      error: (err) => {
        const msg = err?.error?.message || err?.error?.error || err?.message || '';
        const base = this.isEdit
          ? 'No se pudo actualizar la instancia.'
          : 'No se pudo crear la instancia.';
        this.error = msg ? `${base} ${msg}` : base;
        this.saving = false;
        this.cdr.detectChanges();
        console.error('Instance save error:', err);
      }
    });
  }

  cancel(): void {
    this.showForm = false;
    this.form.reset({ active: true });
    this.error = null;
  }

  private afterSave(): void {
    this.saving = false;
    this.showForm = false;
    this.form.reset({ active: true });
    this.error = null;

    this.loading = true;
    this.cdr.detectChanges();

    setTimeout(() => {
      this.loadInstances();
    }, 200);
  }

  deleteInstance(instance: Instance): void {
    if (!confirm(`Eliminar la instancia "${instance.name}"?`)) {
      return;
    }

    this.instanceService.delete(instance.id!).subscribe({
      next: () => {
        this.loadInstances();
      },
      error: (err) => {
        if (err.error?.message) {
          alert(err.error.message);
        } else {
          alert('No se pudo eliminar la instancia');
        }
      }
    });
  }

  trackById(index: number, item: Instance): number {
    return item.id;
  }
}
