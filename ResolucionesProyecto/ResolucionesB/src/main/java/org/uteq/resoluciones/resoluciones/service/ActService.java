package org.uteq.resoluciones.resoluciones.service;

import org.uteq.resoluciones.resoluciones.dto.ActCreateRequest;
import org.uteq.resoluciones.resoluciones.dto.ActResponse;

import java.util.List;

public interface ActService {
    List<ActResponse> listAll();
    List<ActResponse> listBySession(Long sessionId);
    ActResponse create(ActCreateRequest req);
    ActResponse close(Long actId);
}