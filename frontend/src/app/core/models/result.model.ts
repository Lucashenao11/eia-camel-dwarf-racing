export type ResultStatus = 'FINISHED' | 'DISQUALIFIED' | 'DID_NOT_FINISH' | 'DID_NOT_START';

export interface RaceResult {
  id: number;
  registrationId: number;
  participantName: string;
  startingPosition: number | null;
  finalPosition: number | null;
  completionTimeSeconds: number | null;
  penaltyTimeSeconds: number;
  status: ResultStatus;
  notes: string | null;
  recordedAt: string;
}

export interface RaceResultRequest {
  registrationId: number;
  finalPosition?: number;
  completionTimeSeconds?: number;
  penaltyTimeSeconds?: number;
  status: ResultStatus;
  notes?: string;
}

export interface StandingEntry {
  participantId: number;
  participantName: string;
  participantType: 'COMPETITOR' | 'TEAM';
  points: number;
}