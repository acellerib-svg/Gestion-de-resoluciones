package org.uteq.resoluciones.resoluciones.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.resoluciones.resoluciones.dto.InstanceSimpleResponse;
import org.uteq.resoluciones.resoluciones.dto.MeResponse;
import org.uteq.resoluciones.resoluciones.entities.Instance;
import org.uteq.resoluciones.resoluciones.entities.Rol;
import org.uteq.resoluciones.resoluciones.entities.User;
import org.uteq.resoluciones.resoluciones.entities.UserRol;
import org.uteq.resoluciones.resoluciones.repository.InstanceRepository;
import org.uteq.resoluciones.resoluciones.repository.PermissionRepository;
import org.uteq.resoluciones.resoluciones.repository.RolRepository;
import org.uteq.resoluciones.resoluciones.repository.UserRepository;
import org.uteq.resoluciones.resoluciones.repository.UserRolRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepo;
    private final InstanceRepository instanceRepo;
    private final RolRepository rolRepo;
    private final PermissionRepository permissionRepo;
    private final UserRolRepository userRolRepo;

    @Override
    public MeResponse me(Long userId, Long instanceId) {

        User u = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + userId));

        Instance inst = instanceRepo.findById(instanceId)
                .orElseThrow(() -> new EntityNotFoundException("Instancia no encontrada: " + instanceId));

        // ✅ Roles activos (consulta plana)
        List<String> roles = rolRepo.findActiveRolesForUserInstance(userId, instanceId)
                .stream()
                .map(Rol::getName)
                .toList();

        // ✅ Permisos efectivos (mejor traer SOLO codes)
        List<String> permissions = permissionRepo.findEffectivePermissionCodes(userId, instanceId);

        return MeResponse.builder()
                .userId(u.getId())
                .username(u.getUser())
                .names(u.getNames())
                .surnames(u.getSurnames())
                .instanceId(inst.getId())
                .instanceName(inst.getName())
                .roles(roles)
                .permissions(permissions)
                .build();
    }

    @Override
    public List<InstanceSimpleResponse> getUserInstances(Long userId) {
        List<UserRol> userRols = userRolRepo.findByUser4_Id(userId);
        return userRols.stream()
                .map(ur -> ur.getInstance4())
                .collect(Collectors.toMap(Instance::getId, inst -> inst, (a, b) -> a))
                .values().stream()
                .map(inst -> InstanceSimpleResponse.builder()
                        .id(inst.getId())
                        .name(inst.getName())
                        .build())
                .toList();
    }
}