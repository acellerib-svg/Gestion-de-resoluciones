export interface ResolutionResponse {
  id: number;
  resolutionNumber: string;
  topic: string;
  removed: boolean;
  actId: number;
  currentInstanceId: number;
  currentInstanceName: string;
  stateId: number;
  stateName: string;
  creationDate: string;
  updateDate: string;
  // Detail fields
  antecedent?: string;
  resolution?: string;
  fundament?: string;
  createdByUserId?: number;
  createdByName?: string;
  updatedByUserId?: number;
  updatedByName?: string;
}

export interface HistoryEntry {
  id: number;
  action: string;
  date: string;
  observations: string;
  instanceOriginId: number;
  instanceOriginName: string;
  instanceDestinationId: number | null;
  instanceDestinationName: string | null;
  userId: number;
  username: string;
}

export interface VersionEntry {
  id: number;
  version: number;
  content: string;
  date: string;
  userId: number;
  userName: string;
  reason: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
