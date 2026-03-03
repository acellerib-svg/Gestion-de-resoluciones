package org.uteq.resoluciones.resoluciones.service;

import org.uteq.resoluciones.resoluciones.dto.PermissionResponse;

import java.util.List;
import java.util.Set;

public interface RolPermissionService {
    List<PermissionResponse> listPermissionsOfRole(Long roleId);
    void replacePermissions(Long roleId, Set<Long> permissionIds);
}
