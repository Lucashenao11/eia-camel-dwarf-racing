export type Role = 'ADMINISTRATOR' | 'RACE_ORGANIZER' | 'VIEWER';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  username: string;
  role: Role;
}