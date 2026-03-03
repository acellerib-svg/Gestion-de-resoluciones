package org.uteq.resoluciones.resoluciones.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uteq.resoluciones.resoluciones.dto.HierarchicalLevelRequest;
import org.uteq.resoluciones.resoluciones.entities.Hierarchicallevel;
import org.uteq.resoluciones.resoluciones.service.HierarchicalLevelService;

import java.util.List;

@RestController
@RequestMapping("/api/hierarchical-levels")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class HierarchicalLevelController {

    private final HierarchicalLevelService hierarchicalLevelService;

    // =========================
    // LISTAR TODO
    // =========================
    @GetMapping
    public ResponseEntity<List<Hierarchicallevel>> findAll() {
        List<Hierarchicallevel> list = hierarchicalLevelService.findAll();
        return ResponseEntity.ok(list);
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Hierarchicallevel> findById(@PathVariable Long id) {
        Hierarchicallevel level = hierarchicalLevelService.findById(id);
        return ResponseEntity.ok(level);
    }

    // =========================
    // CREAR
    // =========================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Hierarchicallevel> create(
            @Valid @RequestBody HierarchicalLevelRequest request
    ) {
        Hierarchicallevel created = hierarchicalLevelService.create(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // =========================
    // ACTUALIZAR
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<Hierarchicallevel> update(
            @PathVariable Long id,
            @Valid @RequestBody HierarchicalLevelRequest request
    ) {
        Hierarchicallevel updated = hierarchicalLevelService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    // =========================
    // ELIMINAR (borrado logico)
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hierarchicalLevelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
