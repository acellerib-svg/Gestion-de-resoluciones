import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserRolesByInstanceResponse, UserRolesByInstanceUpdateRequest } from '../models/user-roles.model';

@Injectable({ providedIn: 'root' })
export class UserRolesService {
  private baseUrl = '/api/user-roles';

  constructor(private http: HttpClient) {}

  getRoles(userId: number, instanceId: number): Observable<UserRolesByInstanceResponse> {
    const params = new HttpParams()
      .set('userId', userId)
      .set('instanceId', instanceId);
    return this.http.get<UserRolesByInstanceResponse>(this.baseUrl, { params });
  }

  replaceRoles(userId: number, payload: UserRolesByInstanceUpdateRequest): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${userId}`, payload);
  }
}
