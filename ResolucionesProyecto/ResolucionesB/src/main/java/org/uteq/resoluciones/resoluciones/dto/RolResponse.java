package org.uteq.resoluciones.resoluciones.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RolResponse {

    private Long id;
    private String name;
    private String description;
    private Boolean active;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
}
