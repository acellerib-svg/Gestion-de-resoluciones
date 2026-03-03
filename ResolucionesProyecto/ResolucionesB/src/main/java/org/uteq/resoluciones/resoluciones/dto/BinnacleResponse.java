package org.uteq.resoluciones.resoluciones.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class BinnacleResponse {
    private Long id;
    private Long userId;
    private String userName;
    private Long resolutionId;
    private String resolutionNumber;
    private String action;
    private LocalDateTime date;
    private String ip;
    private String userAgent;
}
