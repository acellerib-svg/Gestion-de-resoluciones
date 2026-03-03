package org.uteq.resoluciones.resoluciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HierarchicalLevelRequest {

    @NotBlank
    private String name;

    @NotNull
    private Integer levelOrder;

    private String description;

    @NotNull
    private Boolean active;
}
