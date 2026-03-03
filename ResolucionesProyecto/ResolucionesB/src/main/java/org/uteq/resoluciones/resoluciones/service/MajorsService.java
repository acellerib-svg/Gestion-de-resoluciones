package org.uteq.resoluciones.resoluciones.service;

import org.uteq.resoluciones.resoluciones.entities.Majors;
import java.util.List;

public interface MajorsService {
    List<Majors> findAll();
    List<Majors> findByFacultyId(Long facultyId);
    Majors findById(Long id);
    Majors create(Majors major);
    Majors update(Long id, Majors major);
    void delete(Long id);
}
