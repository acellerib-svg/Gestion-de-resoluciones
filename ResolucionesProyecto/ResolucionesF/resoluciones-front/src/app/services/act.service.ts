import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ActResponse {
  id: number;
  sessionId: number;
  sessionNumber?: string;
  numberAct: string;
  closed: boolean;
  creationDate?: string;
  closingDate?: string;
  observations?: string;
  updateDate?: string;
}

export interface ActCreateRequest {
  sessionId: number;
  closed?: boolean;
  observations?: string;
}

@Injectable({ providedIn: 'root' })
export class ActService {
  private baseUrl = '/api/acts';

  constructor(private http: HttpClient) {}

  listAll(): Observable<ActResponse[]> {
    return this.http.get<ActResponse[]>(this.baseUrl);
  }

  listBySession(sessionId: number): Observable<ActResponse[]> {
    const params = new HttpParams().set('sessionId', sessionId);
    return this.http.get<ActResponse[]>(this.baseUrl, { params });
  }

  create(req: ActCreateRequest): Observable<ActResponse> {
    return this.http.post<ActResponse>(this.baseUrl, req);
  }

  close(actId: number): Observable<ActResponse> {
    return this.http.patch<ActResponse>(`${this.baseUrl}/${actId}/close`, {});
  }
}
