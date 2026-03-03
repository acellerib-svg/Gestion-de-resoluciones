import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService, InstanceSimple } from '../auth/auth.service';
import { NotificationFrontService } from '../services/notification.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css'],
})
export class LayoutComponent implements OnInit, OnDestroy {
  seguridadAbierto = false;
  configuracionAbierto = false;
  unreadCount = 0;
  instances: InstanceSimple[] = [];
  activeInstanceId: number | null = null;
  private pollTimer: any = null;

  constructor(
    public auth: AuthService,
    private router: Router,
    private notifService: NotificationFrontService
  ) {}

  ngOnInit() {
    this.loadUnreadCount();
    this.pollTimer = setInterval(() => this.loadUnreadCount(), 30000);
    this.activeInstanceId = this.auth.getActiveInstanceId();
    this.loadUserInstances();
  }

  ngOnDestroy() {
    if (this.pollTimer) clearInterval(this.pollTimer);
  }

  loadUserInstances() {
    const user = this.auth.getSession();
    if (!user) return;
    this.auth.loadMyInstances(user.id).subscribe({
      next: (list) => this.instances = list,
      error: () => {},
    });
  }

  onInstanceChange(newId: string) {
    const id = Number(newId);
    const user = this.auth.getSession();
    if (!user || !id) return;
    this.auth.loadMe(user.id, id).subscribe({
      next: () => {
        this.activeInstanceId = id;
        this.router.navigate(['/inicio']);
      },
      error: () => {},
    });
  }

  loadUnreadCount() {
    const user = this.auth.getSession();
    if (!user) return;
    this.notifService.countUnread(user.id).subscribe({
      next: (resp) => this.unreadCount = resp?.count ?? 0,
      error: () => {}
    });
  }

  toggleSeguridad() {
    this.seguridadAbierto = !this.seguridadAbierto;
  }

  toggleConfiguracion() {
    this.configuracionAbierto = !this.configuracionAbierto;
  }

  get userName(): string {
    return this.auth.getDisplayName();
  }

  can(permission: string): boolean {
    return this.auth.hasPermission(permission);
  }

  canAny(permissions: string[]): boolean {
    return this.auth.hasAnyPermission(permissions);
  }

  get showSeguridad(): boolean {
    return this.canAny(['USER_MANAGE', 'ROLE_MANAGE', 'PERMISSION_MANAGE', 'USER_ROLE_MANAGE']);
  }

  get showConfiguracion(): boolean {
    return this.canAny(['INSTANCE_MANAGE', 'LEVEL_MANAGE', 'FACULTY_MANAGE', 'MAJOR_MANAGE']);
  }

  cerrarSesion(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
