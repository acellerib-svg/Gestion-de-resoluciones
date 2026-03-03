package org.uteq.resoluciones.resoluciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uteq.resoluciones.resoluciones.entities.Majors;

import java.util.List;

public interface MajorsRepository extends JpaRepository<Majors, Long> {
    List<Majors> findByFacultyId(Long facultyId);
}
