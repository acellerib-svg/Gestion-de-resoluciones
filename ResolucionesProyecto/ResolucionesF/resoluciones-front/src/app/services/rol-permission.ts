import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Permission } from '../models/permission.model';

@Injectable({ providedIn: 'root' })
export class RolPermissionService {
  private baseUrl = '/api/roles';

  constructor(private http: HttpClient) {}

  listByRol(rolId: number): Observable<Permission[]> {
    return this.http.get<Permission[]>(`${this.baseUrl}/${rolId}/permissions`);
  }

  replace(rolId: number, permissionIds: number[]): Observable<void> {
    return this.http.put<void>(
      `${this.baseUrl}/${rolId}/permissions`,
      { permissionIds }
    );
  }
}
