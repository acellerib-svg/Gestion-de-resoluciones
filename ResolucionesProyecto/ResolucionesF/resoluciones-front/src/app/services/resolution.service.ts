import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ResolutionResponse, PageResponse, HistoryEntry, VersionEntry } from '../models/resolution.model';

export interface ResolutionCreateRequest {
  actId: number;
  antecedent: string;
  resolution: string;
  fundament?: string | null;
  currentInstanceId: number;
  topic?: string | null;
  createdByUserId?: number | null;
}

export interface ResolutionActionRequest {
  userId: number;
  observations?: string;
  destinationInstanceId?: number;
}

@Injectable({ providedIn: 'root' })
export class ResolutionService {
  private baseUrl = '/api/resolutions';

  constructor(private http: HttpClient) {}

  list(page: number, size: number, stateId?: number, instanceId?: number, actId?: number): Observable<PageResponse<ResolutionResponse>> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (stateId != null) params = params.set('stateId', stateId);
    if (instanceId != null) params = params.set('instanceId', instanceId);
    if (actId != null) params = params.set('actId', actId);
    return this.http.get<PageResponse<ResolutionResponse>>(this.baseUrl, { params });
  }

  getById(id: number): Observable<ResolutionResponse> {
    return this.http.get<ResolutionResponse>(`${this.baseUrl}/${id}`);
  }

  create(req: ResolutionCreateRequest): Observable<ResolutionResponse> {
    return this.http.post<ResolutionResponse>(this.baseUrl, req);
  }

  update(id: number, body: { antecedent?: string; resolution?: string; fundament?: string; topic?: string; userId: number }): Observable<ResolutionResponse> {
    return this.http.put<ResolutionResponse>(`${this.baseUrl}/${id}`, body);
  }

  approve(id: number, req: ResolutionActionRequest): Observable<ResolutionResponse> {
    return this.http.post<ResolutionResponse>(`${this.baseUrl}/${id}/approve`, req);
  }

  reject(id: number, req: ResolutionActionRequest): Observable<ResolutionResponse> {
    return this.http.post<ResolutionResponse>(`${this.baseUrl}/${id}/reject`, req);
  }

  transfer(id: number, req: ResolutionActionRequest): Observable<ResolutionResponse> {
    return this.http.post<ResolutionResponse>(`${this.baseUrl}/${id}/transfer`, req);
  }

  archive(id: number, req: ResolutionActionRequest): Observable<ResolutionResponse> {
    return this.http.post<ResolutionResponse>(`${this.baseUrl}/${id}/archive`, req);
  }

  reopen(id: number, req: ResolutionActionRequest): Observable<ResolutionResponse> {
    return this.http.post<ResolutionResponse>(`${this.baseUrl}/${id}/reopen`, req);
  }

  getHistory(id: number): Observable<HistoryEntry[]> {
    return this.http.get<HistoryEntry[]>(`${this.baseUrl}/${id}/history`);
  }

  getVersions(id: number): Observable<VersionEntry[]> {
    return this.http.get<VersionEntry[]>(`${this.baseUrl}/${id}/versions`);
  }

  revertVersion(resolutionId: number, versionId: number, userId: number): Observable<VersionEntry> {
    return this.http.post<VersionEntry>(`${this.baseUrl}/${resolutionId}/versions/${versionId}/revert`, null, {
      params: { userId }
    });
  }

  delete(id: number, userId: number) {
    return this.http.delete(`${this.baseUrl}/${id}`, { params: { userId } });
  }
}
