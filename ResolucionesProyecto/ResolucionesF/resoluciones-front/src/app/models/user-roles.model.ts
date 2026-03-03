import { Rol } from './rol.model';

export interface UserRolesByInstanceResponse {
  userId: number;
  instanceId: number;
  instanceName: string;
  roles: Rol[];
}

export interface UserRolesByInstanceUpdateRequest {
  instanceId: number;
  roleIds: number[];
}
