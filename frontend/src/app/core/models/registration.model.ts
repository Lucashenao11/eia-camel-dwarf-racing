export type RegistrationStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'CANCELLED';

export interface RaceRegistration {
  id: number;
  raceId: number;
  raceName: string;
  competitorId: number | null;
  competitorNickname: string | null;
  teamId: number | null;
  teamName: string | null;
  registrationDate: string;
  status: RegistrationStatus;
  startingPosition: number | null;
  validationNotes: string | null;
}

export interface RegistrationRequest {
  competitorId?: number;
  teamId?: number;
}