package org.uteq.resoluciones.resoluciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.resoluciones.resoluciones.entities.Rol;

import java.util.List;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    // =========================
    // CREATE usando procedimiento almacenado
    // =========================
    @Modifying
    @Transactional
    @Query(value = "CALL sp_create_rol(:name, :description, :active)", nativeQuery = true)
    void createRol(
            @Param("name") String name,
            @Param("description") String description,
            @Param("active") Boolean active
    );

    // =========================
    // UPDATE usando procedimiento almacenado
    // =========================
    @Modifying
    @Transactional
    @Query(value = "CALL sp_update_rol(:id, :name, :description, :active)", nativeQuery = true)
    void updateRol(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("description") String description,
            @Param("active") Boolean active
    );

    // =========================
    // TOGGLE STATUS usando procedimiento almacenado
    // =========================
    @Modifying
    @Transactional
    @Query(value = "CALL sp_toggle_rol_status(:id)", nativeQuery = true)
    void toggleRolStatus(@Param("id") Long id);

    // =========================
    // VALIDACIONES (sin procedimiento)
    // =========================
    boolean existsByName(String name);

    // =========================
    // 🔐 ROLES ACTIVOS POR USUARIO E INSTANCIA
    // =========================
    @Query("""
        select distinct r
        from UserRol ur
        join ur.rol r
        where ur.user4.id = :userId
          and ur.instance4.id = :instanceId
          and r.active = true
        order by r.name
    """)
    List<Rol> findActiveRolesForUserInstance(
            @Param("userId") Long userId,
            @Param("instanceId") Long instanceId
    );
}