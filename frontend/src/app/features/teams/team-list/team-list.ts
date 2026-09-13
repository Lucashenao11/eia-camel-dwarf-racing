import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TeamService } from '../../../core/services/team.service';
import { AuthService } from '../../../core/services/auth.service';
import { Team } from '../../../core/models/team.model';
import { FormsModule } from '@angular/forms';
import { ConfirmService } from '../../../core/services/confirm.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-team-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './team-list.html'
})
export class TeamList implements OnInit {
  teams: Team[] = [];
  loading = true;
  errorMessage = '';
  showForm = false;
  newTeam = { name: '', description: '', coach: '' };

  constructor(
    private teamService: TeamService,
    public authService: AuthService,
    private confirmService: ConfirmService,
    private toastService: ToastService
  ) { }
  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.teamService.getAll(0, 20).subscribe({
      next: (result) => { this.teams = result.content; this.loading = false; },
      error: () => { this.errorMessage = 'Could not load teams.'; this.loading = false; }
    });
  }

  createTeam(): void {
    this.teamService.create(this.newTeam).subscribe({
      next: () => {
        this.newTeam = { name: '', description: '', coach: '' };
        this.showForm = false;
        this.toastService.success('Team created.');
        this.load();
      },
      error: (err) => this.toastService.error(err.error?.message || 'Create failed.')
    });
  }

  async deleteTeam(id: number): Promise<void> {
    const confirmed = await this.confirmService.confirm('Delete this team?');
    if (!confirmed) return;
    this.teamService.delete(id).subscribe({
      next: () => { this.toastService.success('Team deleted.'); this.load(); },
      error: (err) => this.toastService.error(err.error?.message || 'Delete failed.')
    });
  }
}