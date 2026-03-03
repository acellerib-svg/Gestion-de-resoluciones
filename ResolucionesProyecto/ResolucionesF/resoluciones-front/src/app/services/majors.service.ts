import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Major {
  id: number;
  name: string;
  description: string;
  active: boolean;
  faculty: { id: number; name: string };
  creationDate: string;
  updateDate: string;
}

export interface MajorRequest {
  name: string;
  description?: string;
  active: boolean;
  faculty: { id: number };
}

@Injectable({ providedIn: 'root' })
export class MajorsService {
  private baseUrl = '/api/majors';

  constructor(private http: HttpClient) {}

  list(): Observable<Major[]> {
    return this.http.get<Major[]>(this.baseUrl);
  }

  listByFaculty(facultyId: number): Observable<Major[]> {
    return this.http.get<Major[]>(`${this.baseUrl}?facultyId=${facultyId}`);
  }

  getById(id: number): Observable<Major> {
    return this.http.get<Major>(`${this.baseUrl}/${id}`);
  }

  create(req: MajorRequest): Observable<Major> {
    return this.http.post<Major>(this.baseUrl, req);
  }

  update(id: number, req: MajorRequest): Observable<Major> {
    return this.http.put<Major>(`${this.baseUrl}/${id}`, req);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
