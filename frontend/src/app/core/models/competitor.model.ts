export type CompetitorType = 'DWARF' | 'CAMEL' | 'MEDIUM' | 'OTHER';
export type CompetitorStatus = 'ACTIVE' | 'INJURED' | 'SUSPENDED' | 'RETIRED';

export interface Competitor {
  id: number;
  name: string;
  nickname: string;
  type: CompetitorType;
  dateOfBirth: string;
  weight: number;
  height: number;
  countryOfOrigin: string;
  status: CompetitorStatus;
  registrationDate: string;
}

export interface CompetitorRequest {
  name: string;
  nickname: string;
  type: CompetitorType;
  dateOfBirth: string;
  weight: number;
  height: number;
  countryOfOrigin: string;
}