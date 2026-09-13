import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CompetitorService } from '../../../core/services/competitor.service';

@Component({
  selector: 'app-competitor-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './competitor-form.html'
})
export class CompetitorForm implements OnInit {
  form: FormGroup;
  isEditMode = false;
  competitorId: number | null = null;
  errorMessage = '';
  loading = false;

  constructor(
    private fb: FormBuilder,
    private competitorService: CompetitorService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.fb.group({
      name: ['', Validators.required],
      nickname: ['', Validators.required],
      type: ['', Validators.required],
      dateOfBirth: ['', Validators.required],
      weight: [null, [Validators.required, Validators.min(0.01)]],
      height: [null, [Validators.required, Validators.min(0.01)]],
      countryOfOrigin: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEditMode = true;
      this.competitorId = Number(idParam);
      this.competitorService.getById(this.competitorId).subscribe({
        next: (c) => this.form.patchValue({
          name: c.name, nickname: c.nickname, type: c.type,
          dateOfBirth: c.dateOfBirth, weight: c.weight, height: c.height,
          countryOfOrigin: c.countryOfOrigin
        }),
        error: () => this.errorMessage = 'Could not load competitor.'
      });
      // Type, date of birth, and country are immutable after creation — disable in edit mode
      this.form.get('type')?.disable();
      this.form.get('dateOfBirth')?.disable();
      this.form.get('countryOfOrigin')?.disable();
    }
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.errorMessage = '';

    const payload = { ...this.form.getRawValue() };
    const request = this.isEditMode
      ? this.competitorService.update(this.competitorId!, payload)
      : this.competitorService.create(payload);

    request.subscribe({
      next: () => this.router.navigate(['/competitors']),
      error: (err) => {
        this.errorMessage = err.error?.message || 'Save failed.';
        this.loading = false;
      }
    });
  }
}