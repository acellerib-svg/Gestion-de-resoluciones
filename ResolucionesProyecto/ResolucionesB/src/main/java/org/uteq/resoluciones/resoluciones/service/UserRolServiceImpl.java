package org.uteq.resoluciones.resoluciones.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.resoluciones.resoluciones.dto.RolResponse;
import org.uteq.resoluciones.resoluciones.dto.UserRolesByInstanceResponse;
import org.uteq.resoluciones.resoluciones.entities.Instance;
import org.uteq.resoluciones.resoluciones.entities.Rol;
import org.uteq.resoluciones.resoluciones.entities.User;
import org.uteq.resoluciones.resoluciones.entities.UserRol;
import org.uteq.resoluciones.resoluciones.entities.UserRolId;
import org.uteq.resoluciones.resoluciones.repository.InstanceRepository;
import org.uteq.resoluciones.resoluciones.repository.RolRepository;
import org.uteq.resoluciones.resoluciones.repository.UserRepository;
import org.uteq.resoluciones.resoluciones.repository.UserRolRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRolServiceImpl implements UserRolService {

    private final UserRolRepository userRolRepo;
    private final UserRepository userRepo;
    private final RolRepository rolRepo;
    private final InstanceRepository instanceRepo;

    @Override
    @Transactional(readOnly = true)
    public UserRolesByInstanceResponse getRoles(Long userId, Long instanceId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + userId));

        Instance inst = instanceRepo.findById(instanceId)
                .orElseThrow(() -> new EntityNotFoundException("Instancia no encontrada: " + instanceId));

        List<UserRol> links = userRolRepo.findByUser4_IdAndInstance4_Id(userId, instanceId);

        List<RolResponse> roles = links.stream()
                .map(UserRol::getRol)
                .map(r -> RolResponse.builder()
                        .id(r.getId())
                        .name(r.getName())
                        .description(r.getDescription())
                        .active(r.getActive())
                        .creationDate(r.getCreationDate())
                        .updateDate(r.getUpdateDate())
                        .build()
                ).toList();

        return UserRolesByInstanceResponse.builder()
                .userId(user.getId())
                .instanceId(inst.getId())
                .instanceName(inst.getName())
                .roles(roles)
                .build();
    }

    @Override
    public void replaceRoles(Long userId, Long instanceId, Set<Long> roleIds) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + userId));

        Instance inst = instanceRepo.findById(instanceId)
                .orElseThrow(() -> new EntityNotFoundException("Instancia no encontrada: " + instanceId));

        // borra roles actuales de esa instancia
        userRolRepo.deleteByUser4_IdAndInstance4_Id(userId, instanceId);

        // inserta nuevos
        for (Long roleId : roleIds) {
            Rol rol = rolRepo.findById(roleId)
                    .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + roleId));

            UserRol link = new UserRol();
            // ✅ ID compuesto incluye instanceId
            link.setId(new UserRolId(userId, roleId, instanceId));
            link.setUser4(user);
            link.setRol(rol);
            link.setInstance4(inst);

            userRolRepo.save(link);
        }
    }

    @Override
    public void addRole(Long userId, Long instanceId, Long roleId) {

        if (userRolRepo.existsByUser4_IdAndRol_IdAndInstance4_Id(userId, roleId, instanceId)) {
            return; // ya existe, no hace nada
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + userId));

        Instance inst = instanceRepo.findById(instanceId)
                .orElseThrow(() -> new EntityNotFoundException("Instancia no encontrada: " + instanceId));

        Rol rol = rolRepo.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + roleId));

        UserRol link = new UserRol();
        // ✅ ID compuesto incluye instanceId
        link.setId(new UserRolId(userId, roleId, instanceId));
        link.setUser4(user);
        link.setRol(rol);
        link.setInstance4(inst);

        userRolRepo.save(link);
    }

    @Override
    public void removeRole(Long userId, Long instanceId, Long roleId) {
        userRolRepo.deleteByUser4_IdAndRol_IdAndInstance4_Id(userId, roleId, instanceId);
    }
}
