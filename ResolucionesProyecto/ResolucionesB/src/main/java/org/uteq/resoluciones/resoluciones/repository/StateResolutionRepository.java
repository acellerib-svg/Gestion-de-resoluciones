package org.uteq.resoluciones.resoluciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uteq.resoluciones.resoluciones.entities.StateResolution;

import java.util.Optional;

public interface StateResolutionRepository extends JpaRepository<StateResolution, Long>{

    Optional<StateResolution> findByNameIgnoreCase(String Name);
}
