package org.uteq.resoluciones.resoluciones.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.uteq.resoluciones.resoluciones.entities.Resolution;

public interface ResolutionRepository extends JpaRepository<Resolution,Long>{

    Page<Resolution> findByRemovedFalse(Pageable pageable);
    Page<Resolution> findByRemovedFalseAndState_StateResolution(Long stateResolution, Pageable pageable);
    Page<Resolution> findByRemovedFalseAndCurrentInstanceId(Long instanceId, Pageable pageable);
    Page<Resolution> findByRemovedFalseAndAct1Id(Long actId, Pageable pageable);

    long countByRemovedFalse();
    long countByRemovedFalseAndState_NameIgnoreCase(String name);
    long countByCurrentInstanceId(Long instanceId);
}
