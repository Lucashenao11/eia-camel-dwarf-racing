import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navbar } from './shared/navbar/navbar';
import { Toast } from './shared/toast/toast';
import { ConfirmDialog } from './shared/confirm-dialog/confirm-dialog';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Navbar, Toast, ConfirmDialog],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {}