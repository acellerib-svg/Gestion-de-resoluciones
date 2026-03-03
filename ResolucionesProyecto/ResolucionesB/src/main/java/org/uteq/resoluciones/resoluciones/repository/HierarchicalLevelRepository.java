package org.uteq.resoluciones.resoluciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uteq.resoluciones.resoluciones.entities.Hierarchicallevel;

import java.util.List;

public interface HierarchicalLevelRepository extends JpaRepository<Hierarchicallevel, Long> {

    List<Hierarchicallevel> findByActiveTrueOrderByOrderAsc();

    List<Hierarchicallevel> findAllByOrderByOrderAsc();
}
