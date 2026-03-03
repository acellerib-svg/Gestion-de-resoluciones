import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface NotificationEntry {
  id: number;
  message: string;
  date: string;
  state: boolean;
  readIn: string | null;
  channel: string;
}

@Injectable({ providedIn: 'root' })
export class NotificationFrontService {
  private baseUrl = '/api/notifications';
  constructor(private http: HttpClient) {}

  listByUser(userId: number): Observable<NotificationEntry[]> {
    return this.http.get<NotificationEntry[]>(this.baseUrl, { params: { userId } });
  }

  countUnread(userId: number): Observable<{ count: number }> {
    return this.http.get<{ count: number }>(`${this.baseUrl}/count`, { params: { userId } });
  }

  markAsRead(id: number): Observable<NotificationEntry> {
    return this.http.patch<NotificationEntry>(`${this.baseUrl}/${id}/read`, {});
  }

  markAllAsRead(userId: number): Observable<void> {
    return this.http.patch<void>(`${this.baseUrl}/read-all`, {}, { params: { userId } });
  }
}
