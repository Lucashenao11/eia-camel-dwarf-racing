import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { RaceResult, RaceResultRequest, StandingEntry } from '../models/result.model';
import { Page } from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class ResultService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getForRace(raceId: number, page = 0, size = 20): Observable<Page<RaceResult>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<RaceResult>>(`${this.baseUrl}/races/${raceId}/results`, { params });
  }

  record(raceId: number, data: RaceResultRequest): Observable<RaceResult> {
    return this.http.post<RaceResult>(`${this.baseUrl}/races/${raceId}/results`, data);
  }

  update(id: number, data: RaceResultRequest): Observable<RaceResult> {
    return this.http.put<RaceResult>(`${this.baseUrl}/results/${id}`, data);
  }

  getOverallStandings(): Observable<StandingEntry[]> {
    return this.http.get<StandingEntry[]>(`${this.baseUrl}/standings`);
  }

  getCompetitorStandings(): Observable<StandingEntry[]> {
    return this.http.get<StandingEntry[]>(`${this.baseUrl}/standings/competitors`);
  }

  getTeamStandings(): Observable<StandingEntry[]> {
    return this.http.get<StandingEntry[]>(`${this.baseUrl}/standings/teams`);
  }
}