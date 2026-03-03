import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface BinnacleEntry {
  id: number;
  userId: number;
  userName: string;
  resolutionId: number | null;
  resolutionNumber: number | null;
  action: string;
  date: string;
  ip: string;
  userAgent: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

@Injectable({ providedIn: 'root' })
export class BinnacleService {
  private baseUrl = '/api/binnacle';
  constructor(private http: HttpClient) {}

  list(page: number, size: number, resolutionId?: number, userId?: number): Observable<PageResponse<BinnacleEntry>> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (resolutionId != null) params = params.set('resolutionId', resolutionId);
    if (userId != null) params = params.set('userId', userId);
    return this.http.get<PageResponse<BinnacleEntry>>(this.baseUrl, { params });
  }
}
