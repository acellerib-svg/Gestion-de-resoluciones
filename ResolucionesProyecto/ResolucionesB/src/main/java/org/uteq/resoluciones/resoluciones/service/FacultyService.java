package org.uteq.resoluciones.resoluciones.service;

import org.uteq.resoluciones.resoluciones.entities.Faculty;
import java.util.List;

public interface FacultyService {
    List<Faculty> findAll();
    Faculty findById(Long id);
    Faculty create(Faculty faculty);
    Faculty update(Long id, Faculty faculty);
    void delete(Long id);
}
