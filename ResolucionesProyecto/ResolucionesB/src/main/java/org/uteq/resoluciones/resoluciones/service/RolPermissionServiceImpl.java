package org.uteq.resoluciones.resoluciones.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.resoluciones.resoluciones.config.PermissionMapper;
import org.uteq.resoluciones.resoluciones.dto.PermissionResponse;
import org.uteq.resoluciones.resoluciones.entities.Permission;
import org.uteq.resoluciones.resoluciones.entities.Rol;
import org.uteq.resoluciones.resoluciones.repository.PermissionRepository;
import org.uteq.resoluciones.resoluciones.repository.RolRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RolPermissionServiceImpl implements RolPermissionService {

    private final RolRepository rolRepo;
    private final PermissionRepository permRepo;

    @Override
    @Transactional(readOnly = true)
    public List<PermissionResponse> listPermissionsOfRole(Long roleId) {
        Rol rol = rolRepo.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + roleId));
        return rol.getPermissions().stream().map(PermissionMapper::toDto).toList();
    }

    @Override
    public void replacePermissions(Long roleId, Set<Long> permissionIds) {
        Rol rol = rolRepo.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + roleId));

        List<Permission> perms = permRepo.findAllById(permissionIds);
        if (perms.size() != permissionIds.size()) {
            Set<Long> found = perms.stream().map(Permission::getId).collect(Collectors.toSet());
            Set<Long> missing = permissionIds.stream().filter(id -> !found.contains(id)).collect(Collectors.toSet());
            throw new RuntimeException("Permisos no encontrados: " + missing);
        }

        rol.getPermissions().clear();
        rol.getPermissions().addAll(perms);
        rolRepo.save(rol);
    }
}
