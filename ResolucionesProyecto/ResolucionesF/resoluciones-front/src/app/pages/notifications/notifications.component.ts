import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { finalize } from 'rxjs/operators';
import { NotificationFrontService, NotificationEntry } from '../../services/notification.service';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {
  loading = false;
  error: string | null = null;

  notifications: NotificationEntry[] = [];
  userId: number | null = null;

  constructor(
    private service: NotificationFrontService,
    private auth: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const session = this.auth.getSession();
    this.userId = session?.id ?? null;
    if (this.userId) {
      this.load();
    }
  }

  load(): void {
    if (!this.userId) return;

    this.loading = true;
    this.error = null;
    this.cdr.detectChanges();

    this.service
      .listByUser(this.userId)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: (res) => {
          this.notifications = Array.isArray(res) ? res : [];
          this.cdr.detectChanges();
        },
        error: () => {
          this.error = 'No se pudieron cargar las notificaciones';
          this.cdr.detectChanges();
        }
      });
  }

  markAsRead(notification: NotificationEntry): void {
    if (notification.state) return; // ya leida

    this.service.markAsRead(notification.id).subscribe({
      next: (updated) => {
        const idx = this.notifications.findIndex(n => n.id === updated.id);
        if (idx >= 0) {
          this.notifications[idx] = updated;
        }
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'No se pudo marcar como leida';
        this.cdr.detectChanges();
      }
    });
  }

  markAllAsRead(): void {
    if (!this.userId) return;

    this.loading = true;
    this.cdr.detectChanges();

    this.service
      .markAllAsRead(this.userId)
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
          this.error = 'No se pudieron marcar todas como leidas';
          this.cdr.detectChanges();
        }
      });
  }

  get unreadCount(): number {
    return this.notifications.filter(n => !n.state).length;
  }

  formatDate(iso?: string): string {
    if (!iso) return '';
    const d = new Date(iso);
    return d.toLocaleString();
  }

  trackById(_index: number, item: NotificationEntry): number {
    return item.id;
  }
}
