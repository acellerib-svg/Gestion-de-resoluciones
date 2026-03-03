package org.uteq.resoluciones.resoluciones.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uteq.resoluciones.resoluciones.dto.InstanceRequest;
import org.uteq.resoluciones.resoluciones.dto.InstanceResponse;
import org.uteq.resoluciones.resoluciones.entities.Hierarchicallevel;
import org.uteq.resoluciones.resoluciones.entities.Instance;
import org.uteq.resoluciones.resoluciones.entities.Majors;
import org.uteq.resoluciones.resoluciones.service.InstanceService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/instance")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class InstanceController {

    private final InstanceService instanceService;

    // =========================
    // LISTAR TODO
    // =========================
    @GetMapping
    public ResponseEntity<List<InstanceResponse>> findAll() {
        List<InstanceResponse> list = instanceService.findAll().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(list);
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<InstanceResponse> findById(@PathVariable Long id) {
        Instance instance = instanceService.findById(id);
        return ResponseEntity.ok(toResponse(instance));
    }

    // =========================
    // FILTRAR POR LEVEL
    // =========================
    @GetMapping("/level/{idLevel}")
    public ResponseEntity<List<InstanceResponse>> findByLevel(@PathVariable Long idLevel) {
        List<InstanceResponse> list = instanceService.findByLevelId(idLevel).stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(list);
    }

    // =========================
    // FILTRAR POR PADRE
    // =========================
    @GetMapping("/father/{idFather}")
    public ResponseEntity<List<InstanceResponse>> findByFather(@PathVariable Long idFather) {
        List<InstanceResponse> list = instanceService.findByInstanceFatherId(idFather).stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(list);
    }

    // =========================
    // CREAR
    // =========================
    @PostMapping
    public ResponseEntity<InstanceResponse> save(
            @Valid @RequestBody InstanceRequest request
    ) {
        Instance instance = new Instance();

        instance.setName(request.getName());
        instance.setDescription(request.getDescription());
        instance.setActive(request.getActive());

        Hierarchicallevel level = new Hierarchicallevel();
        level.setId(request.getLevelId());
        instance.setLevel(level);

        if (request.getMajorId() != null) {
            Majors major = new Majors();
            major.setId(request.getMajorId());
            instance.setMajor(major);
        }

        if (request.getInstanceFatherId() != null) {
            Instance father = new Instance();
            father.setId(request.getInstanceFatherId());
            instance.setInstanceFather(father);
        }

        instance.setCreationDate(LocalDateTime.now());
        instance.setUpdateDate(LocalDateTime.now());

        Instance saved = instanceService.save(instance);

        return new ResponseEntity<>(toResponse(saved), HttpStatus.CREATED);
    }

    // =========================
    // ACTUALIZAR
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<InstanceResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody InstanceRequest request
    ) {
        Instance instanceDb = instanceService.findById(id);

        instanceDb.setName(request.getName());
        instanceDb.setDescription(request.getDescription());
        instanceDb.setActive(request.getActive());

        Hierarchicallevel level = new Hierarchicallevel();
        level.setId(request.getLevelId());
        instanceDb.setLevel(level);

        if (request.getMajorId() != null) {
            Majors major = new Majors();
            major.setId(request.getMajorId());
            instanceDb.setMajor(major);
        } else {
            instanceDb.setMajor(null);
        }

        if (request.getInstanceFatherId() != null) {
            Instance father = new Instance();
            father.setId(request.getInstanceFatherId());
            instanceDb.setInstanceFather(father);
        } else {
            instanceDb.setInstanceFather(null);
        }

        instanceDb.setUpdateDate(LocalDateTime.now());

        Instance updated = instanceService.save(instanceDb);
        return ResponseEntity.ok(toResponse(updated));
    }

    // =========================
    // ELIMINAR (lógico)
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        instanceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // MAPPER Entity -> Response
    // =========================
    private InstanceResponse toResponse(Instance instance) {
        return InstanceResponse.builder()
                .id(instance.getId())
                .name(instance.getName())
                .description(instance.getDescription())
                .active(instance.getActive())
                .creationDate(instance.getCreationDate())
                .updateDate(instance.getUpdateDate())

                .levelId(instance.getLevel() != null ? instance.getLevel().getId() : null)
                .levelName(instance.getLevel() != null ? instance.getLevel().getName() : null)

                .majorId(instance.getMajor() != null ? instance.getMajor().getId() : null)
                .majorName(instance.getMajor() != null ? instance.getMajor().getName() : null)

                .instanceFatherId(instance.getInstanceFather() != null ? instance.getInstanceFather().getId() : null)
                .instanceFatherName(instance.getInstanceFather() != null ? instance.getInstanceFather().getName() : null)
                .build();
    }
}
