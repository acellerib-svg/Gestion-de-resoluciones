package org.uteq.resoluciones.resoluciones.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VersionResolutionResponse {
    private Long id;
    private Integer version;
    private String content;
    private LocalDateTime date;
    private Long userId;
    private String userName;
    private String reason;
}
