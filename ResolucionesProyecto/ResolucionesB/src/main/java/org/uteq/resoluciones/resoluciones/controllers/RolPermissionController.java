package org.uteq.resoluciones.resoluciones.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.uteq.resoluciones.resoluciones.dto.PermissionResponse;
import org.uteq.resoluciones.resoluciones.dto.RolePermissionsUpdateRequest;
import org.uteq.resoluciones.resoluciones.service.RolPermissionService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RolPermissionController {

    private final RolPermissionService service;

    @GetMapping("/{id}/permissions")
    public List<PermissionResponse> list(@PathVariable Long id) {
        return service.listPermissionsOfRole(id);
    }

    @PutMapping("/{id}/permissions")
    public void replace(@PathVariable Long id, @Valid @RequestBody RolePermissionsUpdateRequest req) {
        service.replacePermissions(id, req.getPermissionIds());
    }
}
