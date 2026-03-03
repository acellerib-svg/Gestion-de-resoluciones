package org.uteq.resoluciones.resoluciones.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.resoluciones.resoluciones.entities.Rol;
import org.uteq.resoluciones.resoluciones.repository.RolRepository;

import java.util.List;

@Service
@Transactional
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    // ======================
    // CREATE
    // ======================
    @Override
    public void create(Rol rol) {
        // Validar nombre duplicado
        if (rolRepository.existsByName(rol.getName())) {
            throw new RuntimeException("Ya existe un rol con ese nombre");
        }

        // Llamar al procedimiento almacenado
        rolRepository.createRol(
                rol.getName(),
                rol.getDescription(),
                rol.getActive() != null ? rol.getActive() : true
        );
    }

    // ======================
    // UPDATE
    // ======================
    @Override
    public void update(Long id, Rol rol) {
        // Verificar que existe
        Rol existingRol = findById(id);

        // Llamar al procedimiento almacenado
        rolRepository.updateRol(
                id,
                rol.getName(),
                rol.getDescription(),
                rol.getActive()
        );
    }

    // ======================
    // ACTIVATE / DEACTIVATE
    // ======================
    @Override
    public void toggleStatus(Long id) {
        // Verificar que existe
        findById(id);

        // Llamar al procedimiento almacenado
        rolRepository.toggleRolStatus(id);
    }

    // ======================
    // READ
    // ======================
    @Override
    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    @Override
    public Rol findById(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Rol no encontrado con id: " + id));
    }
}