export interface Permission {
  id?: number;
  code: string;
  name: string;
  description?: string | null;
  module: string;
  active: boolean;

  creationDate?: string;
  updateDate?: string;
}
