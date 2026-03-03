import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface LoginApiResponse {
  id: number;
  username: string;
  names: string;
  surnames: string;
  token: string;
  profileCompleted: boolean;
}

export interface UserSession {
  id: number;
  username: string;
  names: string;
  surnames: string;
  profileCompleted: boolean;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = '/api/auth';

  constructor(private http: HttpClient) {}

  // ✅ login backend (incluye token)
  login(username: string, password: string): Observable<LoginApiResponse> {
    return this.http.post<LoginApiResponse>(`${this.baseUrl}/login`, { username, password });
  }

  // ✅ guarda token + user
  saveSession(resp: LoginApiResponse): void {
    localStorage.setItem('token', resp.token);

    const user: UserSession = {
      id: resp.id,
      username: resp.username,
      names: resp.names,
      surnames: resp.surnames,
      profileCompleted: resp.profileCompleted,
    };
    localStorage.setItem('user', JSON.stringify(user));
  }

  getSession(): UserSession | null {
    const raw = localStorage.getItem('user');
    return raw ? (JSON.parse(raw) as UserSession) : null;
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // ✅ para tu guard (si lo usas)
  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  // ✅ para mostrar el nombre en la UI
  getDisplayName(): string {
    const u = this.getSession();
    if (!u) return 'Usuario';

    const full = `${u.names ?? ''} ${u.surnames ?? ''}`.trim();
    return full || u.username || 'Usuario';
  }

  logout(): void {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    localStorage.removeItem('activeInstanceId');
    localStorage.removeItem('security');
  }
}
