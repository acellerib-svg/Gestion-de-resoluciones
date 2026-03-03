package org.uteq.resoluciones.resoluciones.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResolutionUpdateRequest {
    private String antecedent;
    private String resolution;
    private String fundament;
    @Size(max = 200)
    private String topic;
    private Long userId;
}
