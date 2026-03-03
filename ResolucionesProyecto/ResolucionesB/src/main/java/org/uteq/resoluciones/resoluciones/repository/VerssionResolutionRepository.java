package org.uteq.resoluciones.resoluciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uteq.resoluciones.resoluciones.entities.VersionResolution;

import java.util.List;
import java.util.Optional;

public interface VerssionResolutionRepository extends JpaRepository<VersionResolution, Long> {
    List<VersionResolution> findByResolution3IdOrderByVersionDesc(Long resolutionId);
    Optional<VersionResolution> findTopByResolution3IdOrderByVersionDesc(Long resolutionId);
}
