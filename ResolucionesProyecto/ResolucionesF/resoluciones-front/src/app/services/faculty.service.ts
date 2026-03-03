import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Faculty {
  id: number;
  name: string;
  description: string;
  active: boolean;
  creationDate: string;
  updateDate: string;
}

export interface FacultyRequest {
  name: string;
  description?: string;
  active: boolean;
}

@Injectable({ providedIn: 'root' })
export class FacultyService {
  private baseUrl = '/api/faculties';

  constructor(private http: HttpClient) {}

  list(): Observable<Faculty[]> {
    return this.http.get<Faculty[]>(this.baseUrl);
  }

  getById(id: number): Observable<Faculty> {
    return this.http.get<Faculty>(`${this.baseUrl}/${id}`);
  }

  create(req: FacultyRequest): Observable<Faculty> {
    return this.http.post<Faculty>(this.baseUrl, req);
  }

  update(id: number, req: FacultyRequest): Observable<Faculty> {
    return this.http.put<Faculty>(`${this.baseUrl}/${id}`, req);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
