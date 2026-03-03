package org.uteq.resoluciones.resoluciones.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HistoryResponse {
    private Long id;
    private String action;
    private LocalDateTime date;
    private String observations;

    private Long instanceOriginId;
    private String instanceOriginName;

    private Long instanceDestinationId;
    private String instanceDestinationName;

    private Long userId;
    private String username;
}
