package org.uteq.resoluciones.resoluciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uteq.resoluciones.resoluciones.entities.DocumentResolution;

import java.util.List;

public interface DocumentResolutionRepository extends JpaRepository<DocumentResolution, Long> {
    List<DocumentResolution> findByResolution1IdOrderByUploadDateDesc(Long resolutionId);
}
