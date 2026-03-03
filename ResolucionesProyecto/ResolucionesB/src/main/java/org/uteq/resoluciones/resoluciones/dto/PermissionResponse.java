package org.uteq.resoluciones.resoluciones.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PermissionResponse {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String module;
    private Boolean active;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
}