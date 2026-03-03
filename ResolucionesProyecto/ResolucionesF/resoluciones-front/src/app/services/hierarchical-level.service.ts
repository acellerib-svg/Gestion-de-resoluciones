import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface HierarchicalLevel {
  id: number;
  name: string;
  order: number;
  description: string;
  active: boolean;
  creationDate: string;
  updateDate: string;
}

export interface HierarchicalLevelRequest {
  name: string;
  levelOrder: number;
  description?: string;
  active: boolean;
}

@Injectable({ providedIn: 'root' })
export class HierarchicalLevelService {
  private baseUrl = '/api/hierarchical-levels';

  constructor(private http: HttpClient) {}

  list(): Observable<HierarchicalLevel[]> {
    return this.http.get<HierarchicalLevel[]>(this.baseUrl);
  }

  getById(id: number): Observable<HierarchicalLevel> {
    return this.http.get<HierarchicalLevel>(`${this.baseUrl}/${id}`);
  }

  create(req: HierarchicalLevelRequest): Observable<HierarchicalLevel> {
    return this.http.post<HierarchicalLevel>(this.baseUrl, req);
  }

  update(id: number, req: HierarchicalLevelRequest): Observable<HierarchicalLevel> {
    return this.http.put<HierarchicalLevel>(`${this.baseUrl}/${id}`, req);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
