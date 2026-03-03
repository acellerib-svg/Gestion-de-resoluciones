package org.uteq.resoluciones.resoluciones.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.uteq.resoluciones.resoluciones.entities.Binnacle;

public interface BinnacleRepository extends JpaRepository<Binnacle, Long> {
    Page<Binnacle> findByResolution3IdOrderByDateDesc(Long resolutionId, Pageable pageable);
    Page<Binnacle> findByUser2IdOrderByDateDesc(Long userId, Pageable pageable);
    Page<Binnacle> findAllByOrderByDateDesc(Pageable pageable);
}
