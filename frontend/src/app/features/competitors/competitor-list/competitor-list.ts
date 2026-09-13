import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CompetitorService } from '../../../core/services/competitor.service';
import { AuthService } from '../../../core/services/auth.service';
import { Competitor } from '../../../core/models/competitor.model';
import { FormsModule } from '@angular/forms';
import { ConfirmService } from '../../../core/services/confirm.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-competitor-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './competitor-list.html'
})
export class CompetitorList implements OnInit {
  competitors: Competitor[] = [];
  loading = true;
  errorMessage = '';
  page = 0;
  totalPages = 0;
  typeFilter = '';
  statusFilter = '';

  constructor(
    private competitorService: CompetitorService,
    public authService: AuthService,
    private confirmService: ConfirmService,
    private toastService: ToastService
  ) { }
  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.competitorService.getAll(this.page, 10, this.typeFilter || undefined, this.statusFilter || undefined).subscribe({
      next: (result) => {
        this.competitors = result.content;
        this.totalPages = result.totalPages;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Could not load competitors.';
        this.loading = false;
      }
    });
  }

  onFilterChange(): void {
    this.page = 0;
    this.load();
  }

  nextPage(): void {
    if (this.page < this.totalPages - 1) { this.page++; this.load(); }
  }

  prevPage(): void {
    if (this.page > 0) { this.page--; this.load(); }
  }

  async deleteCompetitor(id: number): Promise<void> {
    const confirmed = await this.confirmService.confirm('Delete this competitor? This cannot be undone.');
    if (!confirmed) return;
    this.competitorService.delete(id).subscribe({
      next: () => { this.toastService.success('Competitor deleted.'); this.load(); },
      error: (err) => this.toastService.error(err.error?.message || 'Delete failed.')
    });
  }
}