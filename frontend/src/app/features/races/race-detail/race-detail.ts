import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { RaceService } from '../../../core/services/race.service';
import { AuthService } from '../../../core/services/auth.service';
import { Race, RaceStatus } from '../../../core/models/race.model';
import { ConfirmService } from '../../../core/services/confirm.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-race-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './race-detail.html'
})
export class RaceDetail implements OnInit {
  race: Race | null = null;
  loading = true;
  errorMessage = '';
  raceId!: number;

  // Legal next transitions, mirrored from the backend state machine, for a sensible UI
  private nextStatusMap: Record<RaceStatus, RaceStatus[]> = {
    DRAFT: ['OPEN_FOR_REGISTRATION', 'CANCELLED'],
    OPEN_FOR_REGISTRATION: ['CLOSED_FOR_REGISTRATION', 'CANCELLED'],
    CLOSED_FOR_REGISTRATION: ['IN_PROGRESS', 'CANCELLED'],
    IN_PROGRESS: ['COMPLETED'],
    COMPLETED: [],
    CANCELLED: []
  };

  constructor(
    private route: ActivatedRoute,
    private raceService: RaceService,
    private confirmService: ConfirmService,
    private toastService: ToastService,
    public authService: AuthService
  ) { }

  ngOnInit(): void {
    this.raceId = Number(this.route.snapshot.paramMap.get('id'));
    this.load();
  }

  load(): void {
    this.loading = true;
    this.raceService.getById(this.raceId).subscribe({
      next: (r) => { this.race = r; this.loading = false; },
      error: () => { this.errorMessage = 'Could not load race.'; this.loading = false; }
    });
  }

  get availableTransitions(): RaceStatus[] {
    return this.race ? this.nextStatusMap[this.race.status] : [];
  }

  async changeStatus(status: RaceStatus): Promise<void> {
    const confirmed = await this.confirmService.confirm(`Change race status to ${status}?`);
    if (!confirmed) return;
    this.raceService.updateStatus(this.raceId, status).subscribe({
      next: () => { this.toastService.success('Race status updated.'); this.load(); },
      error: (err) => this.toastService.error(err.error?.message || 'Status change failed.')
    });
  }
}