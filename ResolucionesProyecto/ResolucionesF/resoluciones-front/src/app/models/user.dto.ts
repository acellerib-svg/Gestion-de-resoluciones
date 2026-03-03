export interface UserCreateRequest {
  user: string;
  password: string;
  names: string;
  surnames: string;
  phone?: string | null;
  email: string;
  state: boolean;
}

export interface UserUpdateRequest {
  id: number;
  user: string;
  names: string;
  surnames: string;
  phone?: string | null;
  email: string;
  state: boolean;
}

export interface UserPasswordUpdateRequest {
  password: string;
}
