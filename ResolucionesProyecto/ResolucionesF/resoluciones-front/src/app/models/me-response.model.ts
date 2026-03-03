export interface MeResponse {
  userId: number;
  instanceId: number;
  instanceName?: string;

  // Roles de la app (tb_rol)
  roles: { id: number; name: string }[];

  // Roles DB efectivos (tb_db_role o equivalentes)
  dbRoles: string[];

  // Permisos efectivos (codes)
  permissions: string[];
}
