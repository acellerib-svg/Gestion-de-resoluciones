package org.uteq.resoluciones.resoluciones.service;

import org.uteq.resoluciones.resoluciones.dto.InstanceSimpleResponse;
import org.uteq.resoluciones.resoluciones.dto.MeResponse;

import java.util.List;

public interface SecurityService {
    MeResponse me(Long userId, Long instanceId);
    List<InstanceSimpleResponse> getUserInstances(Long userId);
}