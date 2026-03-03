package org.uteq.resoluciones.resoluciones.service;

import org.uteq.resoluciones.resoluciones.dto.SessionCreateRequest;
import org.uteq.resoluciones.resoluciones.dto.SessionResponse;

import java.util.List;

public interface SessionService {
    List<SessionResponse> list(Long instanceId);
    SessionResponse create(SessionCreateRequest req);
}