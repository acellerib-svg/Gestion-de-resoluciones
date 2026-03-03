export interface User {
  id?: number;
  user: string;
  password?: string; // solo para create / cambio password
  names: string;
  surnames: string;
  phone?: string | null;
  email: string;
  state: boolean;

  creationDate?: string;
  updateDate?: string;
  lastAccess?: string;
}
