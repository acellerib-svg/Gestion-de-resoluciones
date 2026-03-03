package org.uteq.resoluciones.resoluciones.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.uteq.resoluciones.resoluciones.dto.UserRolesByInstanceResponse;
import org.uteq.resoluciones.resoluciones.dto.UserRolesByInstanceUpdateRequest;
import org.uteq.resoluciones.resoluciones.service.UserRolService;

@RestController
@RequestMapping("/api/user-roles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserRolController {

    private final UserRolService service;

    // ✅ Listar roles de un usuario en una instancia:
    // GET /api/user-roles?userId=1&instanceId=2
    @GetMapping
    public UserRolesByInstanceResponse getRoles(
            @RequestParam Long userId,
            @RequestParam Long instanceId
    ) {
        return service.getRoles(userId, instanceId);
    }

    // ✅ Reemplazar roles de un usuario en una instancia
    // PUT /api/user-roles/{userId}
    // body: { "instanceId": 2, "roleIds": [1,2,3] }
    @PutMapping("/{userId}")
    public void replaceRoles(
            @PathVariable Long userId,
            @Valid @RequestBody UserRolesByInstanceUpdateRequest req
    ) {
        service.replaceRoles(userId, req.getInstanceId(), req.getRoleIds());
    }

    // ✅ Agregar un rol
    // POST /api/user-roles/{userId}/instances/{instanceId}/roles/{roleId}
    @PostMapping("/{userId}/instances/{instanceId}/roles/{roleId}")
    public void addRole(
            @PathVariable Long userId,
            @PathVariable Long instanceId,
            @PathVariable Long roleId
    ) {
        service.addRole(userId, instanceId, roleId);
    }

    // ✅ Quitar un rol
    // DELETE /api/user-roles/{userId}/instances/{instanceId}/roles/{roleId}
    @DeleteMapping("/{userId}/instances/{instanceId}/roles/{roleId}")
    public void removeRole(
            @PathVariable Long userId,
            @PathVariable Long instanceId,
            @PathVariable Long roleId
    ) {
        service.removeRole(userId, instanceId, roleId);
    }
}
