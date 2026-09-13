export type RaceType = 'INDIVIDUAL' | 'TEAM' | 'MIXED';
export type RaceStatus = 'DRAFT' | 'OPEN_FOR_REGISTRATION' | 'CLOSED_FOR_REGISTRATION' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';

export interface Race {
  id: number;
  name: string;
  description: string | null;
  scheduledDateTime: string;
  startLocation: string;
  finishLocation: string;
  distanceMeters: number;
  maxParticipants: number;
  type: RaceType;
  status: RaceStatus;
  organizer: string;
  registrationDeadline: string;
  createdAt: string;
  updatedAt: string;
}

export interface RaceRequest {
  name: string;
  description?: string;
  scheduledDateTime: string;
  startLocation: string;
  finishLocation: string;
  distanceMeters: number;
  maxParticipants: number;
  type: RaceType;
  organizer: string;
  registrationDeadline: string;
}