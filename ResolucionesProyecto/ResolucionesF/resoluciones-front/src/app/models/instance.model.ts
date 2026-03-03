export interface Instance {
  id: number;
  name: string;
  description: string;
  active: boolean;
  levelId: number;
  levelName: string;
  majorId?: number;
  majorName?: string;
  instanceFatherId?: number;
  instanceFatherName?: string;
}
