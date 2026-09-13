import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { RaceService } from '../../../core/services/race.service';

@Component({
  selector: 'app-race-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './race-form.html'
})
export class RaceForm {
  form: FormGroup;
  errorMessage = '';
  loading = false;

  constructor(private fb: FormBuilder, private raceService: RaceService, private router: Router) {
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      scheduledDateTime: ['', Validators.required],
      startLocation: ['', Validators.required],
      finishLocation: ['', Validators.required],
      distanceMeters: [null, [Validators.required, Validators.min(0.01)]],
      maxParticipants: [null, [Validators.required, Validators.min(1)]],
      type: ['', Validators.required],
      organizer: ['', Validators.required],
      registrationDeadline: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.errorMessage = '';

    this.raceService.create(this.form.value).subscribe({
      next: (race) => this.router.navigate(['/races', race.id]),
      error: (err) => {
        this.errorMessage = err.error?.message || 'Save failed.';
        this.loading = false;
      }
    });
  }
}