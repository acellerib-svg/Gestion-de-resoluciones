package org.uteq.resoluciones.resoluciones.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.uteq.resoluciones.resoluciones.entities.UserRol;
import org.uteq.resoluciones.resoluciones.entities.UserRolId;

import java.util.List;

public interface UserRolRepository extends JpaRepository<UserRol, UserRolId> {

    List<UserRol> findByUser4_Id(Long userId);

    List<UserRol> findByUser4_IdAndInstance4_Id(Long userId, Long instanceId);

    boolean existsByUser4_IdAndRol_IdAndInstance4_Id(Long userId, Long rolId, Long instanceId);
    @Transactional
    void deleteByUser4_IdAndInstance4_Id(Long userId, Long instanceId);
    @Transactional
    void deleteByUser4_IdAndRol_IdAndInstance4_Id(Long userId, Long rolId, Long instanceId);
}
