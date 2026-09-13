import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { RaceService } from '../../../core/services/race.service';
import { AuthService } from '../../../core/services/auth.service';
import { Race } from '../../../core/models/race.model';

@Component({
  selector: 'app-race-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './race-list.html'
})
export class RaceList implements OnInit {
  races: Race[] = [];
  loading = true;
  errorMessage = '';
  page = 0;
  totalPages = 0;
  statusFilter = '';

  constructor(private raceService: RaceService, public authService: AuthService) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.raceService.getAll(this.page, 10, undefined, this.statusFilter || undefined).subscribe({
      next: (result) => { this.races = result.content; this.totalPages = result.totalPages; this.loading = false; },
      error: () => { this.errorMessage = 'Could not load races.'; this.loading = false; }
    });
  }

  onFilterChange(): void { this.page = 0; this.load(); }
  nextPage(): void { if (this.page < this.totalPages - 1) { this.page++; this.load(); } }
  prevPage(): void { if (this.page > 0) { this.page--; this.load(); } }
}