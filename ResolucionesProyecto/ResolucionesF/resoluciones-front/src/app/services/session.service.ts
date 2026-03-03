import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SessionResponse {
  id: number;
  instanceId: number;
  instanceName: string;
  date: string;
  sessionNumber?: string;
  tip?: string;
  closed: boolean;
  observations?: string;
  creationDate?: string;
}

export interface SessionCreateRequest {
  instanceId: number;
  date: string;
  tip: string;
  closed: boolean;
  observations?: string;
}

@Injectable({ providedIn: 'root' })
export class SessionService {
  private baseUrl = '/api/sessions';

  constructor(private http: HttpClient) {}

  list(instanceId?: number): Observable<SessionResponse[]> {
    let params = new HttpParams();
    if (instanceId != null) params = params.set('instanceId', instanceId);
    return this.http.get<SessionResponse[]>(this.baseUrl, { params });
  }

  create(req: SessionCreateRequest): Observable<SessionResponse> {
    return this.http.post<SessionResponse>(this.baseUrl, req);
  }
}
