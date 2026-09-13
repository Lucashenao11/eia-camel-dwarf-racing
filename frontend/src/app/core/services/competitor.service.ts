import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Competitor, CompetitorRequest } from '../models/competitor.model';
import { Page } from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class CompetitorService {
  private baseUrl = `${environment.apiUrl}/competitors`;

  constructor(private http: HttpClient) {}

  getAll(page = 0, size = 10, type?: string, status?: string): Observable<Page<Competitor>> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (type) params = params.set('type', type);
    if (status) params = params.set('status', status);
    return this.http.get<Page<Competitor>>(this.baseUrl, { params });
  }

  getById(id: number): Observable<Competitor> {
    return this.http.get<Competitor>(`${this.baseUrl}/${id}`);
  }

  create(data: CompetitorRequest): Observable<Competitor> {
    return this.http.post<Competitor>(this.baseUrl, data);
  }

  update(id: number, data: CompetitorRequest): Observable<Competitor> {
    return this.http.put<Competitor>(`${this.baseUrl}/${id}`, data);
  }

  updateStatus(id: number, status: string): Observable<Competitor> {
    return this.http.patch<Competitor>(`${this.baseUrl}/${id}/status`, { status });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}