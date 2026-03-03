import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Instance } from '../models/instance.model';

@Injectable({
  providedIn: 'root'
})
export class InstanceService {

  // URL base del backend (Spring Boot)
  private readonly apiUrl = '/api/instance';

  // Inyectamos HttpClient para poder hacer peticiones HTTP
  constructor(private http: HttpClient) {}

  // 🔹 OBTENER TODAS LAS INSTANCIAS
  getAll(): Observable<Instance[]> {
    return this.http.get<Instance[]>(this.apiUrl);
  }

  // 🔹 OBTENER UNA INSTANCIA POR ID
  getById(id: number): Observable<Instance> {
    return this.http.get<Instance>(`${this.apiUrl}/${id}`);
  }

  // 🔹 CREAR UNA NUEVA INSTANCIA
  create(payload: Partial<Instance>): Observable<Instance> {
    return this.http.post<Instance>(this.apiUrl, payload);
  }

  // 🔹 ACTUALIZAR UNA INSTANCIA EXISTENTE
  update(id: number, payload: Partial<Instance>): Observable<Instance> {
    return this.http.put<Instance>(`${this.apiUrl}/${id}`, payload);
  }

  // 🔹 ELIMINAR UNA INSTANCIA
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // 🔹 BUSCAR INSTANCIAS POR NIVEL
  findByLevel(levelId: number): Observable<Instance[]> {
    return this.http.get<Instance[]>(`${this.apiUrl}/level/${levelId}`);
  }

  // 🔹 BUSCAR INSTANCIAS POR INSTANCIA PADRE
  findByFather(fatherId: number): Observable<Instance[]> {
    return this.http.get<Instance[]>(`${this.apiUrl}/father/${fatherId}`);
  }
}
