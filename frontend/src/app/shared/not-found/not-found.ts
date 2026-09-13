import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-not-found',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="container">
      <div class="card" style="text-align:center">
        <h2>404 — Page Not Found</h2>
        <p>The page you're looking for doesn't exist.</p>
        <a routerLink="/dashboard" class="btn btn-primary">Back to Dashboard</a>
      </div>
    </div>
  `
})
export class NotFound {}