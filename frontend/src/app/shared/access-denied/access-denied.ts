import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-access-denied',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="container">
      <div class="card" style="text-align:center">
        <h2>403 — Access Denied</h2>
        <p>You don't have permission to view this page.</p>
        <a routerLink="/dashboard" class="btn btn-primary">Back to Dashboard</a>
      </div>
    </div>
  `
})
export class AccessDenied {}