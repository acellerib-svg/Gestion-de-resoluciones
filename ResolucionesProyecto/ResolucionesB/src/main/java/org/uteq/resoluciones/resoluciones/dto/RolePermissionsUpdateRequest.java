package org.uteq.resoluciones.resoluciones.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class RolePermissionsUpdateRequest {
    @NotNull
    private Set<Long> permissionIds;
}
