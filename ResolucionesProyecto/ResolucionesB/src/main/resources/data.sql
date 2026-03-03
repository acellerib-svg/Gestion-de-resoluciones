-- =============================================
-- SEED: Estados de Resolucion
-- =============================================
INSERT INTO tb_state_resolution (name, description, active, creation_date)
SELECT 'EN_ELABORACION', 'Resolucion en proceso de elaboracion', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_state_resolution WHERE name = 'EN_ELABORACION');

INSERT INTO tb_state_resolution (name, description, active, creation_date)
SELECT 'APROBADO', 'Resolucion aprobada', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_state_resolution WHERE name = 'APROBADO');

INSERT INTO tb_state_resolution (name, description, active, creation_date)
SELECT 'RECHAZADO', 'Resolucion rechazada', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_state_resolution WHERE name = 'RECHAZADO');

INSERT INTO tb_state_resolution (name, description, active, creation_date)
SELECT 'TRASLADADO', 'Resolucion trasladada a instancia superior', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_state_resolution WHERE name = 'TRASLADADO');

INSERT INTO tb_state_resolution (name, description, active, creation_date)
SELECT 'ARCHIVADO', 'Resolucion archivada', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_state_resolution WHERE name = 'ARCHIVADO');

-- =============================================
-- SEED: Permisos del sistema
-- =============================================
INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'RESOLUTION_VIEW', 'Ver resoluciones', 'Permite ver el listado y detalle de resoluciones', 'RESOLUCIONES', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'RESOLUTION_VIEW');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'RESOLUTION_CREATE', 'Crear resoluciones', 'Permite crear nuevas resoluciones', 'RESOLUCIONES', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'RESOLUTION_CREATE');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'RESOLUTION_EDIT', 'Editar resoluciones', 'Permite editar resoluciones existentes', 'RESOLUCIONES', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'RESOLUTION_EDIT');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'RESOLUTION_DELETE', 'Eliminar resoluciones', 'Permite eliminar resoluciones', 'RESOLUCIONES', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'RESOLUTION_DELETE');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'RESOLUTION_APPROVE', 'Aprobar resoluciones', 'Permite aprobar resoluciones', 'RESOLUCIONES', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'RESOLUTION_APPROVE');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'RESOLUTION_REJECT', 'Rechazar resoluciones', 'Permite rechazar resoluciones', 'RESOLUCIONES', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'RESOLUTION_REJECT');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'RESOLUTION_TRANSFER', 'Trasladar resoluciones', 'Permite trasladar resoluciones a otra instancia', 'RESOLUCIONES', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'RESOLUTION_TRANSFER');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'RESOLUTION_ARCHIVE', 'Archivar resoluciones', 'Permite archivar resoluciones', 'RESOLUCIONES', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'RESOLUTION_ARCHIVE');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'SESSION_VIEW', 'Ver sesiones', 'Permite ver el listado de sesiones', 'SESIONES', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'SESSION_VIEW');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'SESSION_CREATE', 'Crear sesiones', 'Permite crear nuevas sesiones', 'SESIONES', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'SESSION_CREATE');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'ACT_VIEW', 'Ver actas', 'Permite ver actas de sesiones', 'ACTAS', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'ACT_VIEW');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'ACT_CREATE', 'Crear actas', 'Permite crear nuevas actas', 'ACTAS', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'ACT_CREATE');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'ACT_CLOSE', 'Cerrar actas', 'Permite cerrar actas', 'ACTAS', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'ACT_CLOSE');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'USER_MANAGE', 'Gestionar usuarios', 'Permite ver, crear, editar y eliminar usuarios', 'SEGURIDAD', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'USER_MANAGE');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'ROLE_MANAGE', 'Gestionar roles', 'Permite ver, crear, editar roles y asignar permisos', 'SEGURIDAD', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'ROLE_MANAGE');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'PERMISSION_MANAGE', 'Gestionar permisos', 'Permite ver y editar permisos del sistema', 'SEGURIDAD', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'PERMISSION_MANAGE');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'USER_ROLE_MANAGE', 'Asignar roles a usuarios', 'Permite asignar y quitar roles de usuarios', 'SEGURIDAD', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'USER_ROLE_MANAGE');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'INSTANCE_MANAGE', 'Gestionar instancias', 'Permite crear y editar instancias', 'CONFIGURACION', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'INSTANCE_MANAGE');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'LEVEL_MANAGE', 'Gestionar niveles jerarquicos', 'Permite crear y editar niveles jerarquicos', 'CONFIGURACION', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'LEVEL_MANAGE');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'FACULTY_MANAGE', 'Gestionar facultades', 'Permite crear y editar facultades', 'CONFIGURACION', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'FACULTY_MANAGE');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'MAJOR_MANAGE', 'Gestionar carreras', 'Permite crear y editar carreras', 'CONFIGURACION', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'MAJOR_MANAGE');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'BINNACLE_VIEW', 'Ver bitacora', 'Permite ver el registro de movimientos', 'BITACORA', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'BINNACLE_VIEW');

INSERT INTO tb_permission (code, name, description, module, active, creation_date)
SELECT 'NOTIFICATION_VIEW', 'Ver notificaciones', 'Permite ver notificaciones', 'NOTIFICACIONES', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_permission WHERE code = 'NOTIFICATION_VIEW');

-- =============================================
-- SEED: Rol ADMIN
-- =============================================
INSERT INTO tb_rol (name, description, active, creation_date)
SELECT 'ADMIN', 'Administrador del sistema con todos los permisos', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_rol WHERE name = 'ADMIN');

-- =============================================
-- SEED: Asignar TODOS los permisos al rol ADMIN
-- =============================================
INSERT INTO tb_rol_permission (id_rol, id_permission)
SELECT r.id_rol, p.id_permission
FROM tb_rol r, tb_permission p
WHERE r.name = 'ADMIN' AND p.active = true
  AND NOT EXISTS (
    SELECT 1 FROM tb_rol_permission rp
    WHERE rp.id_rol = r.id_rol AND rp.id_permission = p.id_permission
  );

-- =============================================
-- SEED: Nivel jerarquico base
-- =============================================
INSERT INTO tb_hierarchical_level (name, level_order, description, active, creation_date)
SELECT 'Nivel Central', 1, 'Nivel central de la universidad', true, NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_hierarchical_level WHERE name = 'Nivel Central');

-- =============================================
-- SEED: Instancia base (Consejo Academico)
-- =============================================
INSERT INTO tb_instance (name, id_level, description, active, creation_date, update_date)
SELECT 'Consejo Academico',
       (SELECT id_level FROM tb_hierarchical_level WHERE name = 'Nivel Central' LIMIT 1),
       'Instancia principal del Consejo Academico',
       true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_instance WHERE name = 'Consejo Academico');

-- =============================================
-- SEED: Usuario administrador
-- Usuario: admin  |  Contrasena: Admin2024!
-- =============================================
INSERT INTO tb_user_login (name_user, password, names, surname, phone, creation_date, email, state, profile_completed)
SELECT 'admin',
       '$2a$10$34lgaV2..gjDlRRc7LHXHOvZNB7cinsEpgzxT4Ps69XV3hSClQSgK',
       'Administrador', 'Sistema', NULL, NOW(),
       'admin@uteq.edu.ec', true, true
WHERE NOT EXISTS (SELECT 1 FROM tb_user_login WHERE name_user = 'admin');

-- =============================================
-- SEED: Asignar rol ADMIN al usuario admin en la instancia Consejo Academico
-- =============================================
INSERT INTO tb_user_rol (id_user_login, id_rol, id_instance)
SELECT u.id_user_login, r.id_rol, i.id_instance
FROM tb_user_login u, tb_rol r, tb_instance i
WHERE u.name_user = 'admin'
  AND r.name = 'ADMIN'
  AND i.name = 'Consejo Academico'
  AND NOT EXISTS (
    SELECT 1 FROM tb_user_rol ur
    WHERE ur.id_user_login = u.id_user_login
      AND ur.id_rol = r.id_rol
      AND ur.id_instance = i.id_instance
  );
