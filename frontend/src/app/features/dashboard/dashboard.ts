import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { RaceService } from '../../core/services/race.service';
import { AuthService } from '../../core/services/auth.service';
import { Race } from '../../core/models/race.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.html'
})
export class Dashboard implements OnInit {
  upcomingRaces: Race[] = [];
  loading = true;
  errorMessage = '';

  constructor(private raceService: RaceService, public authService: AuthService) {}

  ngOnInit(): void {
    this.raceService.getAll(0, 5).subscribe({
      next: (page) => {
        this.upcomingRaces = page.content;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Could not load races.';
        this.loading = false;
      }
    });
  }
}