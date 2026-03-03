package org.uteq.resoluciones.resoluciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uteq.resoluciones.resoluciones.entities.HistoryResolution;

import java.util.List;

public interface HistoryResolutionRepository extends JpaRepository<HistoryResolution, Long>{

    List<HistoryResolution> findByResolution3IdOrderByDateAsc(Long resolutionId);
}

