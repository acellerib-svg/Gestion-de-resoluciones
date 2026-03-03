package org.uteq.resoluciones.resoluciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.uteq.resoluciones.resoluciones.entities.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByCodeIgnoreCase(String code);
    boolean existsByCodeIgnoreCase(String code);


    @Query("""
        select distinct p
        from UserRol ur
        join ur.rol r
        join r.permissions p
        where ur.user4.id = :userId
          and ur.instance4.id = :instanceId
          and r.active = true
          and p.active = true
        order by p.module, p.code
    """)
    List<Permission> findEffectivePermissions(@Param("userId") Long userId,
                                              @Param("instanceId") Long instanceId);

    @Query("""
        select distinct p.code
        from UserRol ur
        join ur.rol r
        join r.permissions p
        where ur.user4.id = :userId
          and ur.instance4.id = :instanceId
          and r.active = true
          and p.active = true
        order by p.code
    """)
    List<String> findEffectivePermissionCodes(@Param("userId") Long userId,
                                              @Param("instanceId") Long instanceId);
}