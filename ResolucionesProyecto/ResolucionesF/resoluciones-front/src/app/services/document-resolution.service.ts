import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface DocumentResponse {
  id: number;
  resolutionId: number;
  name: string;
  tipMime: string;
  sizeBytes: number;
  hashSha256: string;
  uploadDate: string;
}

@Injectable({ providedIn: 'root' })
export class DocumentResolutionService {

  constructor(private http: HttpClient) {}

  list(resolutionId: number): Observable<DocumentResponse[]> {
    return this.http.get<DocumentResponse[]>(`/api/resolutions/${resolutionId}/documents`);
  }

  upload(resolutionId: number, file: File, userId?: number): Observable<DocumentResponse> {
    const fd = new FormData();
    fd.append('file', file);
    if (userId) fd.append('userId', userId.toString());
    return this.http.post<DocumentResponse>(`/api/resolutions/${resolutionId}/documents`, fd);
  }

  downloadUrl(resolutionId: number, docId: number): string {
    return `/api/resolutions/${resolutionId}/documents/${docId}/download`;
  }

  delete(resolutionId: number, docId: number, userId?: number): Observable<void> {
    const params: any = {};
    if (userId) params.userId = userId.toString();
    return this.http.delete<void>(`/api/resolutions/${resolutionId}/documents/${docId}`, { params });
  }
}
