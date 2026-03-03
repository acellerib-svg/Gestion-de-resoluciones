import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';
import { UserCreateRequest, UserUpdateRequest, UserPasswordUpdateRequest } from '../models/user.dto';

@Injectable({ providedIn: 'root' })
export class UserService {
  private baseUrl = '/api/user';

  constructor(private http: HttpClient) {}

  getAll(): Observable<User[]> {
    return this.http.get<User[]>(this.baseUrl);
  }

  getById(id: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${id}`);
  }

  create(req: UserCreateRequest): Observable<User> {
    return this.http.post<User>(this.baseUrl, req);
  }

  update(id: number, req: UserUpdateRequest): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/${id}`, req);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  updatePassword(id: number, req: UserPasswordUpdateRequest): Observable<void> {
    return this.http.patch<void>(`${this.baseUrl}/${id}/password`, req);
  }
}
