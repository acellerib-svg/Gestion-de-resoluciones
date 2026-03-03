package org.uteq.resoluciones.resoluciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InstanceRequest {

    @NotBlank
    private String name;

    @NotNull
    private Long levelId;

    private Long majorId;          // opcional
    private Long instanceFatherId; // opcional

    private String description;

    @NotNull
    private Boolean active;
}
