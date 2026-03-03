package org.uteq.resoluciones.resoluciones.service;

import org.uteq.resoluciones.resoluciones.entities.Rol;
import java.util.List;

public interface RolService {
    void create(Rol rol);
    void update(Long id, Rol rol);
    void toggleStatus(Long id);
    List<Rol> findAll();
    Rol findById(Long id);
}
