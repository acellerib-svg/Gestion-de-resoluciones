package org.uteq.resoluciones.resoluciones.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.uteq.resoluciones.resoluciones.dto.*;

import java.util.List;

public interface ResolutionService {
    ResolutionResponse create(ResolutionCreateRequest req);
    ResolutionResponse getById(Long resolutionId);
    ResolutionResponse update(Long resolutionId, ResolutionUpdateRequest req);
    Page<ResolutionResponse> list(Long stateId, Long instanceId, Long actId, Pageable pageable);
    ResolutionResponse approve(Long resolutionId, ResolutionActionRequest req);
    ResolutionResponse reject(Long resolutionId, ResolutionActionRequest req);
    ResolutionResponse transfer(Long resolutionId, ResolutionActionRequest req);
    ResolutionResponse archive(Long resolutionId, ResolutionActionRequest req);
    ResolutionResponse reopen(Long resolutionId, ResolutionActionRequest req);
    List<HistoryResponse> history(Long resolutionId);
    void softDelete(Long resolutionId, Long userId);
}
