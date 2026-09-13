import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { RaceRegistration, RegistrationRequest } from '../models/registration.model';
import { Page } from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class RegistrationService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getForRace(raceId: number, page = 0, size = 20): Observable<Page<RaceRegistration>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<RaceRegistration>>(`${this.baseUrl}/races/${raceId}/registrations`, { params });
  }

  register(raceId: number, data: RegistrationRequest): Observable<RaceRegistration> {
    return this.http.post<RaceRegistration>(`${this.baseUrl}/races/${raceId}/registrations`, data);
  }

  approve(id: number): Observable<RaceRegistration> {
    return this.http.patch<RaceRegistration>(`${this.baseUrl}/registrations/${id}/approve`, {});
  }

  reject(id: number, reason: string): Observable<RaceRegistration> {
    return this.http.patch<RaceRegistration>(`${this.baseUrl}/registrations/${id}/reject`, { reason });
  }

  cancel(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/registrations/${id}`);
  }
}