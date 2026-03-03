package org.uteq.resoluciones.resoluciones.service;

import org.uteq.resoluciones.resoluciones.dto.PermissionCreateRequest;
import org.uteq.resoluciones.resoluciones.dto.PermissionResponse;

import java.util.List;

public interface PermissionService {
    List<PermissionResponse> list();
    PermissionResponse create(PermissionCreateRequest req);
    PermissionResponse update(Long id, PermissionCreateRequest req);
    void toggle(Long id);
}
