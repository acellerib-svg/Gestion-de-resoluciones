package org.uteq.resoluciones.resoluciones.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MoveResolutionRequest {

    @NotNull
    private Long destinationInstanceId;

    @NotNull
    private Long userId;

    @Size(max = 500)
    private String observations;
}
