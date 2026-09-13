import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TeamService } from '../../../core/services/team.service';
import { CompetitorService } from '../../../core/services/competitor.service';
import { AuthService } from '../../../core/services/auth.service';
import { Team } from '../../../core/models/team.model';
import { Competitor } from '../../../core/models/competitor.model';
import { ConfirmService } from '../../../core/services/confirm.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-team-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './team-detail.html'
})
export class TeamDetail implements OnInit {
  team: Team | null = null;
  availableCompetitors: Competitor[] = [];
  selectedCompetitorId: number | null = null;
  loading = true;
  errorMessage = '';
  teamId!: number;

  constructor(
    private route: ActivatedRoute,
    private teamService: TeamService,
    private competitorService: CompetitorService,
    private confirmService: ConfirmService,
    private toastService: ToastService,
    public authService: AuthService
  ) { }

  ngOnInit(): void {
    this.teamId = Number(this.route.snapshot.paramMap.get('id'));
    this.load();
    this.competitorService.getAll(0, 100, undefined, 'ACTIVE').subscribe({
      next: (page) => this.availableCompetitors = page.content
    });
  }

  load(): void {
    this.loading = true;
    this.teamService.getById(this.teamId).subscribe({
      next: (t) => { this.team = t; this.loading = false; },
      error: () => { this.errorMessage = 'Could not load team.'; this.loading = false; }
    });
  }

  addMember(): void {
    if (!this.selectedCompetitorId) return;
    this.teamService.addMember(this.teamId, this.selectedCompetitorId).subscribe({
      next: () => { this.selectedCompetitorId = null; this.toastService.success('Member added.'); this.load(); },
      error: (err) => this.toastService.error(err.error?.message || 'Could not add member.')
    });
  }

  async removeMember(competitorId: number): Promise<void> {
    const confirmed = await this.confirmService.confirm('Remove this member from the team?');
    if (!confirmed) return;
    this.teamService.removeMember(this.teamId, competitorId).subscribe({
      next: () => { this.toastService.success('Member removed.'); this.load(); },
      error: (err) => this.toastService.error(err.error?.message || 'Could not remove member.')
    });
  }
}