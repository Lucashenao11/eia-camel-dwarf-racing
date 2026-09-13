import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ResultService } from '../../core/services/result.service';
import { StandingEntry } from '../../core/models/result.model';

@Component({
  selector: 'app-standings',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './standings.html'
})
export class Standings implements OnInit {
  view: 'overall' | 'competitors' | 'teams' = 'overall';
  standings: StandingEntry[] = [];
  loading = true;
  errorMessage = '';

  constructor(private resultService: ResultService) {}

  ngOnInit(): void { this.load(); }

  setView(view: 'overall' | 'competitors' | 'teams'): void {
    this.view = view;
    this.load();
  }

  load(): void {
    this.loading = true;
    const request = this.view === 'overall' ? this.resultService.getOverallStandings()
      : this.view === 'competitors' ? this.resultService.getCompetitorStandings()
      : this.resultService.getTeamStandings();

    request.subscribe({
      next: (data) => { this.standings = data; this.loading = false; },
      error: () => { this.errorMessage = 'Could not load standings.'; this.loading = false; }
    });
  }
}