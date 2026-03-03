import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { RolPermissionService } from '../../services/rol-permission';
import { PermissionService } from '../../services/permission';
import { Permission } from '../../models/permission.model';
import { RolService } from '../../services/rol'; // el tuyo
import { Rol } from '../../models/rol.model';

type Grouped = { module: string; items: Permission[] };

@Component({
  selector: 'app-rol-permissions',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './rol-permissions.html',
  styleUrls: ['./rol-permissions.scss'],
})
export class RolPermissionsComponent implements OnInit {
  rolId!: number;
  rol?: Rol;

  all: Permission[] = [];
  assignedIds = new Set<number>();

  grouped: Grouped[] = [];

  loading = false;
  saving = false;
  error: string | null = null;

  search = '';
  onlyActive = true;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private rolService: RolService,
    private permService: PermissionService,
    private rolPermService: RolPermissionService
  ) {}

  ngOnInit(): void {
    this.rolId = Number(this.route.snapshot.paramMap.get('id'));
    this.load();
  }

  back(): void {
    this.router.navigate(['/roles']);
  }

  load(): void {
    this.loading = true;
    this.error = null;

    // 1) traer rol (opcional pero útil para mostrar nombre)
    this.rolService.getById(this.rolId).subscribe({
      next: (r) => {
        this.rol = r;
        this.cdr.detectChanges();
      },
      error: () => {
        // no es crítico
      }
    });

    // 2) Traer todos los permisos + los asignados
    this.permService.list().subscribe({
      next: (allPerms) => {
        this.all = allPerms ?? [];

        this.rolPermService.listByRol(this.rolId).subscribe({
          next: (assigned) => {
            this.assignedIds = new Set((assigned ?? []).map(p => Number(p.id)));
            this.applyFilters();
            this.loading = false;
            this.cdr.detectChanges();
          },
          error: () => {
            this.loading = false;
            this.error = 'No se pudieron cargar los permisos asignados al rol.';
            this.cdr.detectChanges();
          }
        });
      },
      error: () => {
        this.loading = false;
        this.error = 'No se pudieron cargar los permisos.';
        this.cdr.detectChanges();
      }
    });
  }

  isChecked(p: Permission): boolean {
    return !!p.id && this.assignedIds.has(Number(p.id));
  }

  toggleOne(p: Permission): void {
    if (!p.id) return;
    const id = Number(p.id);
    if (this.assignedIds.has(id)) this.assignedIds.delete(id);
    else this.assignedIds.add(id);
  }

  toggleModule(module: string, checked: boolean): void {
    const items = this.all.filter(p => p.module === module);
    for (const p of items) {
      if (!p.id) continue;
      const id = Number(p.id);
      if (checked) this.assignedIds.add(id);
      else this.assignedIds.delete(id);
    }
    this.cdr.detectChanges();
  }

  isModuleFullyChecked(module: string): boolean {
    const items = this.filteredList().filter(p => p.module === module);
    if (items.length === 0) return false;
    return items.every(p => p.id && this.assignedIds.has(Number(p.id)));
  }

  applyFilters(): void {
    const groupedMap = new Map<string, Permission[]>();
    for (const p of this.filteredList()) {
      const key = p.module ?? 'OTROS';
      if (!groupedMap.has(key)) groupedMap.set(key, []);
      groupedMap.get(key)!.push(p);
    }

    this.grouped = Array.from(groupedMap.entries())
      .sort((a, b) => a[0].localeCompare(b[0]))
      .map(([module, items]) => ({
        module,
        items: items.sort((x, y) => (x.code ?? '').localeCompare(y.code ?? ''))
      }));

    this.cdr.detectChanges();
  }

  private filteredList(): Permission[] {
    const q = (this.search ?? '').trim().toLowerCase();
    return (this.all ?? []).filter(p => {
      if (this.onlyActive && !p.active) return false;
      if (!q) return true;
      return (
        (p.code ?? '').toLowerCase().includes(q) ||
        (p.name ?? '').toLowerCase().includes(q) ||
        (p.module ?? '').toLowerCase().includes(q)
      );
    });
  }

  save(): void {
    if (this.saving) return;
    this.saving = true;
    this.error = null;

    const ids = Array.from(this.assignedIds.values());

    this.rolPermService.replace(this.rolId, ids).subscribe({
      next: () => {
        this.saving = false;
        alert('Permisos actualizados ✅');
        this.cdr.detectChanges();
      },
      error: () => {
        this.saving = false;
        this.error = 'No se pudieron guardar los permisos.';
        this.cdr.detectChanges();
      }
    });
  }
}
