package org.uteq.resoluciones.resoluciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uteq.resoluciones.resoluciones.entities.Act;

import java.util.List;

public interface ActRepository extends JpaRepository<Act, Long>{

    List<Act> findBySession1_Id(Long idSession);
    long countBySession1_Id(Long sessionId);
}
