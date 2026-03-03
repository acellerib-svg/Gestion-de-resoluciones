package org.uteq.resoluciones.resoluciones.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserRolesByInstanceResponse {
    private Long userId;
    private Long instanceId;
    private String instanceName;
    private List<RolResponse> roles;
}
