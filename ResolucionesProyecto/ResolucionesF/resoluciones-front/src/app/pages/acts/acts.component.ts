import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ActService, ActCreateRequest, ActResponse } from '../../services/act.service';
import { SessionService, SessionResponse } from '../../services/session.service';

@Component({
  selector: 'app-acts',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './acts.component.html',
  styleUrls: ['./acts.component.css']
})
export class ActsComponent implements OnInit {
  sessionId!: number;
  sessionInfo: SessionResponse | null = null;

  loading = false;
  error: string | null = null;
  acts: ActResponse[] = [];

  formOpen = false;
  observations = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: ActService,
    private sessionService: SessionService
  ) {}

  ngOnInit(): void {
    this.sessionId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadSession();
    this.load();
  }

  loadSession(): void {
    this.sessionService.list().subscribe({
      next: (sessions) => {
        this.sessionInfo = sessions.find(s => s.id === this.sessionId) ?? null;
      }
    });
  }

  load(): void {
    this.loading = true;
    this.error = null;
    this.service.listBySession(this.sessionId).subscribe({
      next: (res) => {
        this.acts = res ?? [];
        this.loading = false;
      },
      error: () => {
        this.error = 'No se pudieron cargar las actas';
        this.loading = false;
      }
    });
  }

  back(): void {
    this.router.navigate(['/sessions']);
  }

  openForm(): void { this.formOpen = true; }
  closeForm(): void {
    this.formOpen = false;
    this.observations = '';
  }

  create(): void {
    const req: ActCreateRequest = {
      sessionId: this.sessionId,
      closed: false,
      observations: this.observations?.trim() || undefined
    };

    this.loading = true;
    this.error = null;
    this.service.create(req).subscribe({
      next: () => {
        this.loading = false;
        this.closeForm();
        this.load();
      },
      error: () => {
        this.loading = false;
        this.error = 'No se pudo crear el acta';
      }
    });
  }

  closeAct(actId: number): void {
    this.loading = true;
    this.service.close(actId).subscribe({
      next: () => {
        this.loading = false;
        this.load();
      },
      error: () => {
        this.loading = false;
        this.error = 'No se pudo cerrar el acta';
      }
    });
  }

  formatDate(iso?: string): string {
    if (!iso) return '';
    return new Date(iso).toLocaleString();
  }
}
