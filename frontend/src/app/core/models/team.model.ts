import { Competitor } from './competitor.model';

export type TeamStatus = 'ACTIVE' | 'SUSPENDED' | 'DEACTIVATED';

export interface Team {
  id: number;
  name: string;
  description: string | null;
  coach: string;
  creationDate: string;
  status: TeamStatus;
  competitors: Competitor[];
}

export interface TeamRequest {
  name: string;
  description?: string;
  coach: string;
}