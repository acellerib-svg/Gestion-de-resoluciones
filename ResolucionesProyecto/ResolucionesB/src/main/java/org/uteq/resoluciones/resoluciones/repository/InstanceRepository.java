package org.uteq.resoluciones.resoluciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.resoluciones.resoluciones.entities.Instance;

import java.util.List;

public interface InstanceRepository extends JpaRepository<Instance, Long> {

    // =======================
    // CONSULTAS
    // =======================

    List<Instance> findByLevel_Id(Long idLevel);

    List<Instance> findByInstanceFather_Id(Long idInstanceFather);



    // CREATE
    @Modifying
    @Transactional
    @Query(value = "CALL sp_create_instance(" +
            ":name, :description, :levelId, :majorId, :fatherId, :active)",
            nativeQuery = true)
    void createInstance(
            @Param("name") String name,
            @Param("description") String description,
            @Param("levelId") Long levelId,
            @Param("majorId") Long majorId,
            @Param("fatherId") Long fatherId,
            @Param("active") Boolean active
    );

    // UPDATE
    @Modifying
    @Transactional
    @Query(value = "CALL sp_update_instance(" +
            ":id, :name, :description, :levelId, :majorId, :fatherId, :active)",
            nativeQuery = true)
    void updateInstance(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("description") String description,
            @Param("levelId") Long levelId,
            @Param("majorId") Long majorId,
            @Param("fatherId") Long fatherId,
            @Param("active") Boolean active
    );

    // DELETE FÍSICO
    @Modifying
    @Transactional
    @Query(value = "CALL sp_delete_instance(:id)", nativeQuery = true)
    void deleteInstance(@Param("id") Long id);
}
