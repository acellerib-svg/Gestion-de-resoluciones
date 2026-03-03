package org.uteq.resoluciones.resoluciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uteq.resoluciones.resoluciones.entities.TipAction;

import java.util.Optional;

public interface TipActionRepository extends JpaRepository<TipAction, Long>{
    Optional<TipAction> findByNameIgnoreCase(String name);
}
