package org.uteq.resoluciones.resoluciones.service;

import org.uteq.resoluciones.resoluciones.dto.UserRolesByInstanceResponse;

import java.util.Set;

public interface UserRolService {

    UserRolesByInstanceResponse getRoles(Long userId, Long instanceId);

    void replaceRoles(Long userId, Long instanceId, Set<Long> roleIds);

    void addRole(Long userId, Long instanceId, Long roleId);

    void removeRole(Long userId, Long instanceId, Long roleId);
}
