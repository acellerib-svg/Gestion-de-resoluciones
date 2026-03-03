package org.uteq.resoluciones.resoluciones.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.resoluciones.resoluciones.config.PermissionMapper;
import org.uteq.resoluciones.resoluciones.dto.PermissionCreateRequest;
import org.uteq.resoluciones.resoluciones.dto.PermissionResponse;
import org.uteq.resoluciones.resoluciones.entities.Permission;
import org.uteq.resoluciones.resoluciones.repository.PermissionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository repo;

    @Override
    @Transactional(readOnly = true)
    public List<PermissionResponse> list() {
        return repo.findAll().stream().map(PermissionMapper::toDto).toList();
    }

    @Override
    public PermissionResponse create(PermissionCreateRequest req) {
        if (repo.existsByCodeIgnoreCase(req.getCode())) {
            throw new RuntimeException("Ya existe un permiso con code: " + req.getCode());
        }

        Permission p = new Permission();
        p.setCode(req.getCode().trim().toUpperCase());
        p.setName(req.getName().trim());
        p.setDescription(req.getDescription());
        p.setModule(req.getModule().trim().toUpperCase());
        p.setActive(req.getActive());
        p.setCreationDate(LocalDateTime.now());
        p.setUpdateDate(null);

        return PermissionMapper.toDto(repo.save(p));
    }

    @Override
    public PermissionResponse update(Long id, PermissionCreateRequest req) {
        Permission p = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permiso no encontrado: " + id));

        // si cambias code, validar duplicado
        String newCode = req.getCode().trim().toUpperCase();
        if (!p.getCode().equalsIgnoreCase(newCode) && repo.existsByCodeIgnoreCase(newCode)) {
            throw new RuntimeException("Ya existe un permiso con code: " + newCode);
        }

        p.setCode(newCode);
        p.setName(req.getName().trim());
        p.setDescription(req.getDescription());
        p.setModule(req.getModule().trim().toUpperCase());
        p.setActive(req.getActive());
        p.setUpdateDate(LocalDateTime.now());

        return PermissionMapper.toDto(repo.save(p));
    }

    @Override
    public void toggle(Long id) {
        Permission p = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permiso no encontrado: " + id));

        p.setActive(!Boolean.TRUE.equals(p.getActive()));
        p.setUpdateDate(LocalDateTime.now());
        repo.save(p);
    }
}
