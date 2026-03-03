package org.uteq.resoluciones.resoluciones.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.uteq.resoluciones.resoluciones.dto.BinnacleResponse;

public interface BinnacleService {
    Page<BinnacleResponse> list(Long resolutionId, Long userId, Pageable pageable);
}
