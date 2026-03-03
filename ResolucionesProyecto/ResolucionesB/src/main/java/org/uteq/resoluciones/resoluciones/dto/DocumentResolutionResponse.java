package org.uteq.resoluciones.resoluciones.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class DocumentResolutionResponse {
    private Long id;
    private Long resolutionId;
    private String name;
    private String tipMime;
    private Long sizeBytes;
    private String hashSha256;
    private LocalDateTime uploadDate;
}
