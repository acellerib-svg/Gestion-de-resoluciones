package org.uteq.resoluciones.resoluciones.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {
    private Long id;
    private String message;
    private LocalDateTime date;
    private Boolean state;
    private LocalDateTime readIn;
    private String channel;
}
