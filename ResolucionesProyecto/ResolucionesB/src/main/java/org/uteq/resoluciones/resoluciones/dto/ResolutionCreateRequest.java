package org.uteq.resoluciones.resoluciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResolutionCreateRequest {

    @NotNull
    private Long actId;

    @NotBlank
    private String antecedent;

    @NotBlank
    private String resolution;

    private String fundament;

    @NotNull
    private Long currentInstanceId;

    @Size(max = 200)
    private String topic;

    private Long createdByUserId;
}
