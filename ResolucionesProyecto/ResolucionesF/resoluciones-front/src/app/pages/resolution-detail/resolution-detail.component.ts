import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { ResolutionService } from '../../services/resolution.service';
import { ResolutionResponse, HistoryEntry, VersionEntry } from '../../models/resolution.model';
import { DocumentResolutionService, DocumentResponse } from '../../services/document-resolution.service';
import { InstanceService } from '../../services/instance';
import { Instance } from '../../models/instance.model';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-resolution-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './resolution-detail.component.html',
  styleUrls: ['./resolution-detail.component.css']
})
export class ResolutionDetailComponent implements OnInit {
  resolution: ResolutionResponse | null = null;
  history: HistoryEntry[] = [];
  versions: VersionEntry[] = [];
  documents: DocumentResponse[] = [];
  instances: Instance[] = [];
  activeTab = 'contenido';
  loading = true;
  actionLoading = false;
  error: string | null = null;

  // Transfer modal
  showTransferModal = false;
  transferInstanceId: number | null = null;
  transferObservations = '';

  // Action modal
  showActionModal = false;
  actionType = '';
  actionObservations = '';

  // Edit mode
  editing = false;
  editAntecedent = '';
  editResolution = '';
  editFundament = '';
  editTopic = '';

  // Documents
  uploading = false;
  uploadError: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: ResolutionService,
    private docService: DocumentResolutionService,
    private instanceService: InstanceService,
    private auth: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) this.loadResolution(id);
    this.instanceService.getAll().subscribe({
      next: (list) => { this.instances = list; this.cdr.detectChanges(); }
    });
  }

  loadResolution(id: number) {
    this.loading = true;
    this.error = null;
    this.service.getById(id).pipe(finalize(() => { this.loading = false; this.cdr.detectChanges(); }))
      .subscribe({
        next: (r) => {
          this.resolution = r;
          this.loadHistory(id);
          this.loadVersions(id);
          this.loadDocuments(id);
        },
        error: () => this.error = 'No se pudo cargar la resolucion'
      });
  }

  loadHistory(id: number) {
    this.service.getHistory(id).subscribe({ next: (h) => { this.history = h; this.cdr.detectChanges(); } });
  }

  loadVersions(id: number) {
    this.service.getVersions(id).subscribe({ next: (v) => { this.versions = v; this.cdr.detectChanges(); } });
  }

  loadDocuments(id: number) {
    this.docService.list(id).subscribe({
      next: (d) => { this.documents = d; this.cdr.detectChanges(); },
      error: () => { this.documents = []; }
    });
  }

  get userId(): number {
    return this.auth.getSession()?.id ?? 0;
  }

  get canApprove(): boolean {
    return this.resolution?.stateName === 'EN_ELABORACION';
  }
  get canReject(): boolean {
    return this.resolution?.stateName === 'EN_ELABORACION';
  }
  get canTransfer(): boolean {
    const s = this.resolution?.stateName;
    return s === 'EN_ELABORACION' || s === 'APROBADO';
  }
  get canArchive(): boolean {
    return this.resolution?.stateName === 'APROBADO';
  }
  get canReopen(): boolean {
    const s = this.resolution?.stateName;
    return s === 'RECHAZADO' || s === 'TRASLADADO';
  }
  get canEdit(): boolean {
    return this.resolution?.stateName === 'EN_ELABORACION';
  }

  setTab(tab: string) { this.activeTab = tab; }

  // Edit
  startEdit() {
    if (!this.resolution) return;
    this.editAntecedent = this.resolution.antecedent || '';
    this.editResolution = this.resolution.resolution || '';
    this.editFundament = this.resolution.fundament || '';
    this.editTopic = this.resolution.topic || '';
    this.editing = true;
  }

  cancelEdit() { this.editing = false; }

  saveEdit() {
    if (!this.resolution) return;
    this.actionLoading = true;
    this.service.update(this.resolution.id, {
      antecedent: this.editAntecedent,
      resolution: this.editResolution,
      fundament: this.editFundament,
      topic: this.editTopic,
      userId: this.userId
    }).pipe(finalize(() => { this.actionLoading = false; this.cdr.detectChanges(); }))
      .subscribe({
        next: (r) => { this.resolution = r; this.editing = false; this.loadVersions(r.id); },
        error: () => this.error = 'Error al guardar cambios'
      });
  }

  // Actions
  openAction(type: string) {
    this.actionType = type;
    this.actionObservations = '';
    this.showActionModal = true;
  }

  closeAction() { this.showActionModal = false; }

  confirmAction() {
    if (!this.resolution) return;
    this.actionLoading = true;
    const req = { userId: this.userId, observations: this.actionObservations };
    let action$;
    switch (this.actionType) {
      case 'approve': action$ = this.service.approve(this.resolution.id, req); break;
      case 'reject': action$ = this.service.reject(this.resolution.id, req); break;
      case 'archive': action$ = this.service.archive(this.resolution.id, req); break;
      case 'reopen': action$ = this.service.reopen(this.resolution.id, req); break;
      default: return;
    }
    action$.pipe(finalize(() => { this.actionLoading = false; this.showActionModal = false; this.cdr.detectChanges(); }))
      .subscribe({
        next: (r) => { this.resolution = r; this.loadHistory(r.id); },
        error: (e) => this.error = e?.error?.message || 'Error al ejecutar accion'
      });
  }

  // Transfer
  openTransfer() { this.showTransferModal = true; this.transferInstanceId = null; this.transferObservations = ''; }
  closeTransfer() { this.showTransferModal = false; }

  confirmTransfer() {
    if (!this.resolution || !this.transferInstanceId) return;
    this.actionLoading = true;
    this.service.transfer(this.resolution.id, {
      userId: this.userId,
      destinationInstanceId: this.transferInstanceId,
      observations: this.transferObservations
    }).pipe(finalize(() => { this.actionLoading = false; this.showTransferModal = false; this.cdr.detectChanges(); }))
      .subscribe({
        next: (r) => { this.resolution = r; this.loadHistory(r.id); },
        error: (e) => this.error = e?.error?.message || 'Error al trasladar'
      });
  }

  // Documents
  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length || !this.resolution) return;
    const file = input.files[0];
    this.uploading = true;
    this.uploadError = null;
    this.docService.upload(this.resolution.id, file, this.userId)
      .pipe(finalize(() => { this.uploading = false; this.cdr.detectChanges(); }))
      .subscribe({
        next: () => { this.loadDocuments(this.resolution!.id); input.value = ''; },
        error: (e) => { this.uploadError = e?.error?.message || 'Error al subir archivo'; input.value = ''; }
      });
  }

  downloadDoc(doc: DocumentResponse) {
    if (!this.resolution) return;
    const url = this.docService.downloadUrl(this.resolution.id, doc.id);
    window.open(url, '_blank');
  }

  deleteDoc(doc: DocumentResponse) {
    if (!this.resolution || !confirm('Eliminar el documento "' + doc.name + '"?')) return;
    this.docService.delete(this.resolution.id, doc.id, this.userId).subscribe({
      next: () => this.loadDocuments(this.resolution!.id),
      error: (e) => this.uploadError = e?.error?.message || 'Error al eliminar documento'
    });
  }

  formatFileSize(bytes: number): string {
    if (!bytes) return '0 B';
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1048576) return (bytes / 1024).toFixed(1) + ' KB';
    return (bytes / 1048576).toFixed(1) + ' MB';
  }

  // Versions
  revertVersion(v: VersionEntry) {
    if (!this.resolution || !confirm('Revertir a version ' + v.version + '?')) return;
    this.actionLoading = true;
    this.service.revertVersion(this.resolution.id, v.id, this.userId)
      .pipe(finalize(() => { this.actionLoading = false; this.cdr.detectChanges(); }))
      .subscribe({
        next: () => this.loadResolution(this.resolution!.id),
        error: () => this.error = 'Error al revertir version'
      });
  }

  getStateClass(state: string): string {
    switch (state?.toUpperCase()) {
      case 'EN_ELABORACION': return 'badge-elaboracion';
      case 'APROBADO': return 'badge-aprobado';
      case 'RECHAZADO': return 'badge-rechazado';
      case 'TRASLADADO': return 'badge-trasladado';
      case 'ARCHIVADO': return 'badge-archivado';
      default: return '';
    }
  }

  getActionLabel(type: string): string {
    switch (type) {
      case 'approve': return 'Aprobar';
      case 'reject': return 'Rechazar';
      case 'archive': return 'Archivar';
      case 'reopen': return 'Reabrir';
      default: return type;
    }
  }
}
