package org.uteq.resoluciones.resoluciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uteq.resoluciones.resoluciones.entities.Session;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
     List<Session> findByInstance1_Id(Long instanceId);
     long countByInstance1_Id(Long instanceId);
}
