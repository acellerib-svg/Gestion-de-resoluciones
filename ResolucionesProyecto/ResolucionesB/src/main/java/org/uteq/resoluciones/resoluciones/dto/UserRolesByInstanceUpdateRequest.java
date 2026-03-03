package org.uteq.resoluciones.resoluciones.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class UserRolesByInstanceUpdateRequest {

    @NotNull
    private Long instanceId;

    @NotEmpty
    private Set<Long> roleIds;
}
