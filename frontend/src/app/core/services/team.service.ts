import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Team, TeamRequest } from '../models/team.model';
import { Page } from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class TeamService {
  private baseUrl = `${environment.apiUrl}/teams`;

  constructor(private http: HttpClient) {}

  getAll(page = 0, size = 10, status?: string): Observable<Page<Team>> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (status) params = params.set('status', status);
    return this.http.get<Page<Team>>(this.baseUrl, { params });
  }

  getById(id: number): Observable<Team> {
    return this.http.get<Team>(`${this.baseUrl}/${id}`);
  }

  create(data: TeamRequest): Observable<Team> {
    return this.http.post<Team>(this.baseUrl, data);
  }

  update(id: number, data: TeamRequest): Observable<Team> {
    return this.http.put<Team>(`${this.baseUrl}/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  addMember(teamId: number, competitorId: number): Observable<Team> {
    return this.http.post<Team>(`${this.baseUrl}/${teamId}/members/${competitorId}`, {});
  }

  removeMember(teamId: number, competitorId: number): Observable<Team> {
    return this.http.delete<Team>(`${this.baseUrl}/${teamId}/members/${competitorId}`);
  }
}