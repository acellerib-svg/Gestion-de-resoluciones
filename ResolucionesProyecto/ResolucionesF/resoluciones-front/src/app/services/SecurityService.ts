import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface MeResponse {
  userId: number;
  username: string;
  names: string;
  surnames: string;

  instanceId: number;
  instanceName: string;

  roles: string[];
  permissions: string[];
}

@Injectable({
  providedIn: 'root'
})
export class SecurityService {

  private baseUrl = '/api/auth';

  constructor(private http: HttpClient) {}

  // 🔹 Llama al backend para traer permisos efectivos
  me(userId: number, instanceId: number): Observable<MeResponse> {
    const params = new HttpParams()
      .set('userId', userId)
      .set('instanceId', instanceId);

    return this.http.get<MeResponse>(`${this.baseUrl}/me`, { params });
  }

  // 🔹 Guardar seguridad en localStorage
  saveSecurity(data: MeResponse): void {
    localStorage.setItem('security', JSON.stringify(data));
  }

  getSecurity(): MeResponse | null {
    const raw = localStorage.getItem('security');
    return raw ? JSON.parse(raw) : null;
  }

  clearSecurity(): void {
    localStorage.removeItem('security');
  }

  // 🔹 Validar permiso
  hasPermission(code: string): boolean {
    const sec = this.getSecurity();
    return !!sec?.permissions?.includes(code);
  }
}
