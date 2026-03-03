package org.uteq.resoluciones.resoluciones.service;

import org.uteq.resoluciones.resoluciones.dto.VersionResolutionResponse;

import java.util.List;

public interface VersionResolutionService {
    List<VersionResolutionResponse> listByResolution(Long resolutionId);
    VersionResolutionResponse revert(Long resolutionId, Long versionId, Long userId);
}
