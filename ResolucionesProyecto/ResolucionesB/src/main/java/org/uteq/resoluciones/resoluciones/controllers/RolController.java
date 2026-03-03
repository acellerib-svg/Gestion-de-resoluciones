package org.uteq.resoluciones.resoluciones.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.uteq.resoluciones.resoluciones.dto.RolRequest;
import org.uteq.resoluciones.resoluciones.dto.RolResponse;
import org.uteq.resoluciones.resoluciones.entities.Rol;
import org.uteq.resoluciones.resoluciones.service.RolService;

import java.util.List;

@RestController
@RequestMapping("/api/rol")
@CrossOrigin(origins = "http://localhost:4200")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    // ======================
    // LISTAR
    // ======================
    @GetMapping
    public List<RolResponse> findAll() {
        return rolService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ======================
    // BUSCAR POR ID
    // ======================
    @GetMapping("/{id}")
    public RolResponse findById(@PathVariable Long id) {
        return toResponse(rolService.findById(id));
    }

    // ======================
    // CREAR
    // ======================
    @PostMapping
    public void create(@Valid @RequestBody RolRequest request) {

        Rol rol = new Rol();
        rol.setName(request.getName());
        rol.setDescription(request.getDescription());
        rol.setActive(request.getActive());

        rolService.create(rol);
    }

    // ======================
    // ACTUALIZAR
    // ======================
    @PutMapping("/{id}")
    public void update(@PathVariable Long id,
                       @Valid @RequestBody RolRequest request) {

        Rol rol = new Rol();
        rol.setName(request.getName());
        rol.setDescription(request.getDescription());
        rol.setActive(request.getActive());

        rolService.update(id, rol);
    }

    // ======================
    // ACTIVAR / DESACTIVAR
    // ======================
    @PatchMapping("/{id}/toggle")
    public void toggleStatus(@PathVariable Long id) {
        rolService.toggleStatus(id);
    }

    // ======================
    // MAPPER
    // ======================
    private RolResponse toResponse(Rol rol) {
        return RolResponse.builder()
                .id(rol.getId())
                .name(rol.getName())
                .description(rol.getDescription())
                .active(rol.getActive())
                .creationDate(rol.getCreationDate())
                .updateDate(rol.getUpdateDate())
                .build();
    }
}
