package org.uteq.resoluciones.resoluciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PermissionCreateRequest {
    @NotBlank private String code;
    @NotBlank private String name;
    private String description;
    @NotBlank private String module;
    @NotNull private Boolean active;
}