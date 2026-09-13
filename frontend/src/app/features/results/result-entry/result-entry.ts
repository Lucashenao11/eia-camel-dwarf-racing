import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ResultService } from '../../../core/services/result.service';
import { RegistrationService } from '../../../core/services/registration.service';
import { RaceResult, ResultStatus } from '../../../core/models/result.model';
import { RaceRegistration } from '../../../core/models/registration.model';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-result-entry',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './result-entry.html'
})
export class ResultEntry implements OnInit {
  raceId!: number;
  results: RaceResult[] = [];
  approvedRegistrations: RaceRegistration[] = [];
  loading = true;
  errorMessage = '';

  newResult = {
    registrationId: null as number | null,
    finalPosition: null as number | null,
    completionTimeSeconds: null as number | null,
    status: 'FINISHED' as ResultStatus
  };

  constructor(
    private route: ActivatedRoute,
    private resultService: ResultService,
    private registrationService: RegistrationService,
    private toastService: ToastService
  ) { }

  ngOnInit(): void {
    this.raceId = Number(this.route.snapshot.paramMap.get('id'));
    this.load();
  }

  load(): void {
    this.loading = true;
    this.resultService.getForRace(this.raceId, 0, 50).subscribe({
      next: (page) => { this.results = page.content; this.loading = false; },
      error: () => { this.errorMessage = 'Could not load results.'; this.loading = false; }
    });
    this.registrationService.getForRace(this.raceId, 0, 50).subscribe({
      next: (page) => this.approvedRegistrations = page.content.filter(r => r.status === 'APPROVED')
    });
  }

  submitResult(): void {
    if (!this.newResult.registrationId) return;
    this.resultService.record(this.raceId, this.newResult as any).subscribe({
      next: () => {
        this.newResult = { registrationId: null, finalPosition: null, completionTimeSeconds: null, status: 'FINISHED' };
        this.toastService.success('Result recorded.');
        this.load();
      },
      error: (err) => this.toastService.error(err.error?.message || 'Recording failed.')
    });
  }
}