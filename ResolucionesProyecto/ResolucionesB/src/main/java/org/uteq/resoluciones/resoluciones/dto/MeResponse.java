package org.uteq.resoluciones.resoluciones.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MeResponse {
    private Long userId;
    private String username;
    private String names;
    private String surnames;

    private Long instanceId;
    private String instanceName;

    private List<String> roles;        // nombres de roles app (tb_rol)
    private List<String> permissions;  // codes de permisos (tb_permission.code)
}