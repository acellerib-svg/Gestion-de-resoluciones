import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Permission } from '../models/permission.model';

export interface PermissionCreateRequest {
  code: string;
  name: string;
  description?: string;
  module: string;
  active: boolean;
}

export type PermissionResponse = Permission;

@Injectable({ providedIn: 'root' })
export class PermissionService {
  private baseUrl = '/api/permissions';

  constructor(private http: HttpClient) {}

  list(): Observable<PermissionResponse[]> {
    return this.http.get<PermissionResponse[]>(this.baseUrl);
  }

  create(req: PermissionCreateRequest): Observable<PermissionResponse> {
    return this.http.post<PermissionResponse>(this.baseUrl, req);
  }

  update(id: number, req: PermissionCreateRequest): Observable<PermissionResponse> {
    return this.http.put<PermissionResponse>(`${this.baseUrl}/${id}`, req);
  }

  toggle(id: number): Observable<void> {
    return this.http.patch<void>(`${this.baseUrl}/${id}/toggle`, {});
  }
}
