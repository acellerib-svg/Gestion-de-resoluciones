package org.uteq.resoluciones.resoluciones.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActCreateRequest {
    @NotNull private Long sessionId;
    private Boolean closed;
    private String observations;
}