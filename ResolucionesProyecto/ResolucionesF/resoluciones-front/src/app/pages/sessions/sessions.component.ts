import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, NavigationEnd } from '@angular/router';
import { filter, finalize } from 'rxjs/operators';
import { SessionService, SessionCreateRequest, SessionResponse } from '../../services/session.service';
import { InstanceService } from '../../services/instance';
import { Instance } from '../../models/instance.model';

@Component({
  selector: 'app-sessions',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './sessions.component.html',
  styleUrls: ['./sessions.component.css']
})
export class SessionsComponent implements OnInit {
  loading = false;
  error: string | null = null;

  sessions: SessionResponse[] = [];
  instances: Instance[] = [];

  // formulario
  formOpen = false;
  instanceId: number | null = null;
  date = '';
  tip = '';
  closed = false;
  observations = '';

  sessionTypes = ['Ordinaria', 'Extraordinaria', 'Especial', 'Inaugural'];

  constructor(
    private service: SessionService,
    private instanceService: InstanceService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.load();
    this.loadInstances();

    this.router.events
      .pipe(filter(e => e instanceof NavigationEnd))
      .subscribe(() => {
        if (this.router.url.startsWith('/sessions')) {
          this.load();
        }
      });
  }

  loadInstances(): void {
    this.instanceService.getAll().subscribe({
      next: (res) => {
        this.instances = res ?? [];
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
          this.sessions = Array.isArray(res) ? res : [];
          this.cdr.detectChanges();
        },
        error: () => {
          this.error = 'No se pudieron cargar las sesiones';
          this.cdr.detectChanges();
        }
      });
  }

  goActs(sessionId: number): void {
    this.router.navigate(['/sessions', sessionId, 'acts']);
  }

  openForm(): void {
    this.formOpen = true;
  }

  closeForm(): void {
    this.formOpen = false;
    this.instanceId = null;
    this.date = '';
    this.tip = '';
    this.closed = false;
    this.observations = '';
  }

  create(): void {
    if (this.loading) return;

    const inst = Number(this.instanceId);
    const dt = (this.date ?? '').trim();
    const tipo = (this.tip ?? '').trim();

    if (!inst || !dt || !tipo) {
      this.error = 'Completa instancia, fecha y tipo';
      this.cdr.detectChanges();
      return;
    }

    const req: SessionCreateRequest = {
      instanceId: inst,
      date: new Date(dt).toISOString(),
      tip: tipo,
      closed: !!this.closed,
      observations: this.observations?.trim() || undefined,
    };

    this.loading = true;
    this.error = null;
    this.cdr.detectChanges();

    this.service.create(req)
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
          this.error = 'No se pudo crear la sesión';
          this.cdr.detectChanges();
        }
      });
  }

  formatDate(iso?: string): string {
    if (!iso) return '';
    const d = new Date(iso);
    return d.toLocaleString();
  }
}
