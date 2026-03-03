import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Rol } from '../models/rol.model';

@Injectable({
  providedIn: 'root'
})
export class RolService {

  private apiUrl = '/api/rol';

  constructor(private http: HttpClient) {}

  // ======================
  // READ
  // ======================
  getAll(): Observable<Rol[]> {
    return this.http.get<Rol[]>(this.apiUrl);
  }

  getById(id: number): Observable<Rol> {
    return this.http.get<Rol>(`${this.apiUrl}/${id}`);
  }

  // ======================
  // CREATE
  // ======================
  create(rol: Partial<Rol>): Observable<Rol> {
    return this.http.post<Rol>(this.apiUrl, rol);
  }

  // ======================
  // UPDATE
  // ======================
  update(id: number, rol: Partial<Rol>): Observable<Rol> {
    return this.http.put<Rol>(`${this.apiUrl}/${id}`, rol);
  }

  // ======================
  // ACTIVATE / DEACTIVATE
  // ======================
  toggleStatus(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/toggle`, {});
  }
}
