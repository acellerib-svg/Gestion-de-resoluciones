import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface LoginResponse {
  id: number;
  username: string;
  names: string | null;
  surnames: string | null;
  token: string;
  profileCompleted: boolean;
}

export interface MeResponse {
  userId: number;
  instanceId: number;
  instanceName?: string;
  roles: string[];
  permissions: string[];
}

export interface InstanceSimple {
  id: number;
  name: string;
}

// (Opcional) tipo para lo que guardas en localStorage como "user"
// así no mezclas token dentro del "user".
export interface SessionUser {
  id: number;
  username: string;
  names: string | null;
  surnames: string | null;
  profileCompleted: boolean;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = '/api/auth';

  constructor(private http: HttpClient) {}

  // ========= LOGIN =========
  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, { username, password });
  }

  // ========= CARGAR INSTANCIAS DEL USUARIO =========
  loadMyInstances(userId: number): Observable<InstanceSimple[]> {
    const params = new HttpParams().set('userId', userId);
    return this.http.get<InstanceSimple[]>(`${this.baseUrl}/my-instances`, { params });
  }

  // ========= CARGAR PERMISOS EFECTIVOS =========
  loadMe(userId: number, instanceId: number): Observable<MeResponse> {
    const params = new HttpParams()
      .set('userId', userId)
      .set('instanceId', instanceId);

    return this.http.get<MeResponse>(`${this.baseUrl}/me`, { params }).pipe(
      tap((sec) => {
        this.saveSecurity(sec);
        localStorage.setItem('activeInstanceId', String(instanceId));
      })
    );
  }

  // ========= TOKEN STORAGE =========
  saveToken(token: string) {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  clearToken() {
    localStorage.removeItem('token');
  }

  // ========= SESSION STORAGE =========
  // Guarda token + usuario por separado (recomendado)
  saveSession(resp: LoginResponse) {
    // ✅ token para interceptor + backend
    this.saveToken(resp.token);

    // ✅ usuario "limpio" (sin token)
    const user: SessionUser = {
      id: resp.id,
      username: resp.username,
      names: resp.names,
      surnames: resp.surnames,
      profileCompleted: resp.profileCompleted,
    };

    localStorage.setItem('user', JSON.stringify(user));
  }

  getSession(): SessionUser | null {
    const raw = localStorage.getItem('user');
    return raw ? (JSON.parse(raw) as SessionUser) : null;
  }

  saveSecurity(sec: MeResponse) {
    localStorage.setItem('security', JSON.stringify(sec));
  }

  getSecurity(): MeResponse | null {
    const raw = localStorage.getItem('security');
    return raw ? (JSON.parse(raw) as MeResponse) : null;
  }

  clearSession() {
    localStorage.removeItem('user');
    localStorage.removeItem('security');
  }

  logout() {
    this.clearToken();
    this.clearSession();
    localStorage.removeItem('activeInstanceId');
  }

  // ========= HELPERS =========
  // ✅ ahora se basa en TOKEN, no en "user"
  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  hasPermission(code: string): boolean {
    const sec = this.getSecurity();
    return !!sec?.permissions?.includes(code);
  }

  hasAnyPermission(codes: string[]): boolean {
    if (!codes?.length) return true;
    const sec = this.getSecurity();
    const set = new Set(sec?.permissions ?? []);
    return codes.some((c) => set.has(c));
  }

  // (opcional) roles también
  hasRole(roleName: string): boolean {
    const sec = this.getSecurity();
    return !!sec?.roles?.includes(roleName);
  }

  getDisplayName(): string {
    const u = this.getSession();
    if (!u) return 'Usuario';
    const full = `${u.names ?? ''} ${u.surnames ?? ''}`.trim();
    return full || u.username || 'Usuario';
  }

  getActiveInstanceId(): number | null {
    const val = localStorage.getItem('activeInstanceId');
    return val ? Number(val) : null;
  }

  getActiveInstanceName(): string {
    const sec = this.getSecurity();
    return sec?.instanceName ?? 'Sin instancia';
  }
}
