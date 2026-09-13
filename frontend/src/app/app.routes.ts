import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
    { path: 'login', loadComponent: () => import('./features/auth/login/login').then(m => m.Login) },
    { path: 'register', loadComponent: () => import('./features/auth/register/register').then(m => m.Register) },

    {
        path: 'dashboard',
        canActivate: [authGuard],
        loadComponent: () => import('./features/dashboard/dashboard').then(m => m.Dashboard)
    },
    {
        path: 'competitors',
        canActivate: [authGuard, roleGuard(['ADMINISTRATOR', 'RACE_ORGANIZER'])],
        loadComponent: () => import('./features/competitors/competitor-list/competitor-list').then(m => m.CompetitorList)
    },
    {
        path: 'competitors/new',
        canActivate: [authGuard, roleGuard(['ADMINISTRATOR'])],
        loadComponent: () => import('./features/competitors/competitor-form/competitor-form').then(m => m.CompetitorForm)
    },
    {
        path: 'competitors/:id/edit',
        canActivate: [authGuard, roleGuard(['ADMINISTRATOR'])],
        loadComponent: () => import('./features/competitors/competitor-form/competitor-form').then(m => m.CompetitorForm)
    },
    {
        path: 'teams',
        canActivate: [authGuard, roleGuard(['ADMINISTRATOR', 'RACE_ORGANIZER'])],
        loadComponent: () => import('./features/teams/team-list/team-list').then(m => m.TeamList)
    },
    {
        path: 'teams/:id',
        canActivate: [authGuard, roleGuard(['ADMINISTRATOR', 'RACE_ORGANIZER'])],
        loadComponent: () => import('./features/teams/team-detail/team-detail').then(m => m.TeamDetail)
    },
    {
        path: 'races',
        canActivate: [authGuard],
        loadComponent: () => import('./features/races/race-list/race-list').then(m => m.RaceList)
    },
    {
        path: 'races/new',
        canActivate: [authGuard, roleGuard(['ADMINISTRATOR', 'RACE_ORGANIZER'])],
        loadComponent: () => import('./features/races/race-form/race-form').then(m => m.RaceForm)
    },
    {
        path: 'races/:id',
        canActivate: [authGuard],
        loadComponent: () => import('./features/races/race-detail/race-detail').then(m => m.RaceDetail)
    },
    {
        path: 'races/:id/registrations',
        canActivate: [authGuard, roleGuard(['ADMINISTRATOR', 'RACE_ORGANIZER'])],
        loadComponent: () => import('./features/registrations/registration-management/registration-management').then(m => m.RegistrationManagement)
    },
    {
        path: 'races/:id/results',
        canActivate: [authGuard, roleGuard(['ADMINISTRATOR', 'RACE_ORGANIZER'])],
        loadComponent: () => import('./features/results/result-entry/result-entry').then(m => m.ResultEntry)
    },
    {
        path: 'standings',
        canActivate: [authGuard],
        loadComponent: () => import('./features/standings/standings').then(m => m.Standings)
    },
    {
        path: 'profile',
        canActivate: [authGuard],
        loadComponent: () => import('./features/profile/profile').then(m => m.Profile)
    },

    { path: 'access-denied', loadComponent: () => import('./shared/access-denied/access-denied').then(m => m.AccessDenied) },
    { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    { path: '**', loadComponent: () => import('./shared/not-found/not-found').then(m => m.NotFound) }
];