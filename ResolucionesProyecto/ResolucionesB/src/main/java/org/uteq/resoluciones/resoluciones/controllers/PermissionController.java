package org.uteq.resoluciones.resoluciones.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.uteq.resoluciones.resoluciones.dto.PermissionCreateRequest;
import org.uteq.resoluciones.resoluciones.dto.PermissionResponse;
import org.uteq.resoluciones.resoluciones.service.PermissionService;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PermissionController {

    private final PermissionService service;

    @GetMapping
    public List<PermissionResponse> list() {
        return service.list();
    }

    @PostMapping
    public PermissionResponse create(@Valid @RequestBody PermissionCreateRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public PermissionResponse update(@PathVariable Long id, @Valid @RequestBody PermissionCreateRequest req) {
        return service.update(id, req);
    }

    @PatchMapping("/{id}/toggle")
    public void toggle(@PathVariable Long id) {
        service.toggle(id);
    }
}
