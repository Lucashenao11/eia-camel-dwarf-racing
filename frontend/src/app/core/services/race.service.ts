import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Race, RaceRequest } from '../models/race.model';
import { Page } from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class RaceService {
  private baseUrl = `${environment.apiUrl}/races`;

  constructor(private http: HttpClient) {}

  getAll(page = 0, size = 10, type?: string, status?: string): Observable<Page<Race>> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (type) params = params.set('type', type);
    if (status) params = params.set('status', status);
    return this.http.get<Page<Race>>(this.baseUrl, { params });
  }

  getById(id: number): Observable<Race> {
    return this.http.get<Race>(`${this.baseUrl}/${id}`);
  }

  create(data: RaceRequest): Observable<Race> {
    return this.http.post<Race>(this.baseUrl, data);
  }

  update(id: number, data: RaceRequest): Observable<Race> {
    return this.http.put<Race>(`${this.baseUrl}/${id}`, data);
  }

  updateStatus(id: number, status: string): Observable<Race> {
    return this.http.patch<Race>(`${this.baseUrl}/${id}/status`, { status });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}