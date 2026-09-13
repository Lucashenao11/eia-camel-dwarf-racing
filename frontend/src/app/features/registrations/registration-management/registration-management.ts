import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { RegistrationService } from '../../../core/services/registration.service';
import { CompetitorService } from '../../../core/services/competitor.service';
import { TeamService } from '../../../core/services/team.service';
import { RaceRegistration } from '../../../core/models/registration.model';
import { Competitor } from '../../../core/models/competitor.model';
import { Team } from '../../../core/models/team.model';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-registration-management',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './registration-management.html'
})
export class RegistrationManagement implements OnInit {
  raceId!: number;
  registrations: RaceRegistration[] = [];
  competitors: Competitor[] = [];
  teams: Team[] = [];
  selectedCompetitorId: number | null = null;
  selectedTeamId: number | null = null;
  loading = true;
  errorMessage = '';
  rejectReason = '';
  rejectingId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private registrationService: RegistrationService,
    private competitorService: CompetitorService,
    private teamService: TeamService,
    private toastService: ToastService
  ) { }

  ngOnInit(): void {
    this.raceId = Number(this.route.snapshot.paramMap.get('id'));
    this.load();
    this.competitorService.getAll(0, 100, undefined, 'ACTIVE').subscribe(page => this.competitors = page.content);
    this.teamService.getAll(0, 100, 'ACTIVE').subscribe(page => this.teams = page.content);
  }

  load(): void {
    this.loading = true;
    this.registrationService.getForRace(this.raceId, 0, 50).subscribe({
      next: (page) => { this.registrations = page.content; this.loading = false; },
      error: () => { this.errorMessage = 'Could not load registrations.'; this.loading = false; }
    });
  }

  registerCompetitor(): void {
    if (!this.selectedCompetitorId) return;
    this.registrationService.register(this.raceId, { competitorId: this.selectedCompetitorId }).subscribe({
      next: () => { this.selectedCompetitorId = null; this.toastService.success('Competitor registered.'); this.load(); },
      error: (err) => this.toastService.error(err.error?.message || 'Registration failed.')
    });
  }

  registerTeam(): void {
    if (!this.selectedTeamId) return;
    this.registrationService.register(this.raceId, { teamId: this.selectedTeamId }).subscribe({
      next: () => { this.selectedTeamId = null; this.toastService.success('Team registered.'); this.load(); },
      error: (err) => this.toastService.error(err.error?.message || 'Registration failed.')
    });
  }

  approve(id: number): void {
    this.registrationService.approve(id).subscribe({
      next: () => { this.toastService.success('Registration approved.'); this.load(); },
      error: (err) => this.toastService.error(err.error?.message || 'Approval failed.')
    });
  }

  startReject(id: number): void {
    this.rejectingId = id;
    this.rejectReason = '';
  }

  confirmReject(): void {
    if (!this.rejectingId || !this.rejectReason.trim()) return;
    this.registrationService.reject(this.rejectingId, this.rejectReason).subscribe({
      next: () => { this.rejectingId = null; this.toastService.success('Registration rejected.'); this.load(); },
      error: (err) => this.toastService.error(err.error?.message || 'Rejection failed.')
    });
  }
}