import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs/operators';
import { forkJoin } from 'rxjs';

import { UserService } from '../../services/user';
import { InstanceService } from '../../services/instance';
import { RolService } from '../../services/rol';
import { UserRolesService } from '../../services/user-roles';

import { User } from '../../models/user.model';
import { Instance } from '../../models/instance.model';
import { Rol } from '../../models/rol.model';

@Component({
  selector: 'app-user-roles',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-roles.html',
  styleUrls: ['./user-roles.scss']
})
export class UserRolesComponent implements OnInit {

  users: User[] = [];
  instances: Instance[] = [];
  roles: Rol[] = [];

  userId: number | null = null;
  instanceId: number | null = null;

  // roles seleccionados (IDs)
  selected = new Set<number>();

  loading = false;
  saving = false;
  error: string | null = null;
  info: string | null = null;

  constructor(
    private userService: UserService,
    private instanceService: InstanceService,
    private rolService: RolService,
    private userRolesService: UserRolesService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadCombos();
  }

  // ============================
  // CARGA INICIAL DE COMBOS
  // ============================
  loadCombos(): void {
    this.loading = true;
    this.error = null;
    this.info = null;

    forkJoin({
      users: this.userService.getAll(),
      instances: this.instanceService.getAll(),
      roles: this.rolService.getAll(),
    })
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: ({ users, instances, roles }) => {
          this.users = users ?? [];
          this.instances = instances ?? [];
          this.roles = roles ?? [];
          this.cdr.detectChanges();
        },
        error: () => {
          this.users = [];
          this.instances = [];
          this.roles = [];
          this.error = 'No se pudieron cargar usuarios / instancias / roles';
          this.cdr.detectChanges();
        }
      });
  }

  // ============================
  // UTILIDADES
  // ============================
  canLoad(): boolean {
    return this.userId != null && this.instanceId != null;
  }

  clearMessages(): void {
    this.error = null;
    this.info = null;
  }

  // (opcional) cuando cambias user/instance en el select:
  onSelectionChange(): void {
    this.clearMessages();
    this.selected.clear();

    if (this.canLoad()) {
      this.loadUserRoles();
    }
  }

  // ============================
  // CARGAR ROLES DEL USUARIO EN ESA INSTANCIA
  // ============================
  loadUserRoles(): void {
    if (!this.canLoad()) {
      this.error = 'Selecciona usuario e instancia';
      this.cdr.detectChanges();
      return;
    }

    this.loading = true;
    this.clearMessages();
    this.selected.clear();

    this.userRolesService
      .getRoles(this.userId!, this.instanceId!)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: (res) => {
          (res?.roles ?? []).forEach(r => {
            if (r?.id != null) this.selected.add(Number(r.id));
          });
          this.cdr.detectChanges();
        },
        error: () => {
          // dependiendo tu backend, puede devolver vacío o error.
          // aquí lo tratamos como error explícito:
          this.error = 'No se pudieron cargar roles asignados';
          this.cdr.detectChanges();
        }
      });
  }

  // ============================
  // CHECKS
  // ============================
  toggle(roleId: number): void {
    this.clearMessages();
    if (this.selected.has(roleId)) this.selected.delete(roleId);
    else this.selected.add(roleId);
  }

  isChecked(roleId: number): boolean {
    return this.selected.has(roleId);
  }

  // ============================
  // GUARDAR (REEMPLAZAR ROLES)
  // ============================
  save(): void {
    if (!this.canLoad()) {
      this.error = 'Selecciona usuario e instancia';
      this.cdr.detectChanges();
      return;
    }

    this.saving = true;
    this.clearMessages();

    const payload = {
      instanceId: this.instanceId!,
      roleIds: Array.from(this.selected)
    };

    this.userRolesService
      .replaceRoles(this.userId!, payload)
      .pipe(
        finalize(() => {
          this.saving = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: () => {
          this.info = 'Roles guardados correctamente ✅';
          this.cdr.detectChanges();
        },
        error: () => {
          this.error = 'No se pudo guardar la asignación';
          this.cdr.detectChanges();
        }
      });
  }
}
