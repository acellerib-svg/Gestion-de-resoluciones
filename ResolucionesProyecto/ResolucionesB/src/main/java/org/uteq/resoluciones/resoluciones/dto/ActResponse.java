package org.uteq.resoluciones.resoluciones.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ActResponse {

    private Long id;
    private Long sessionId;
    private String sessionNumber;
    private String numberAct;
    private Boolean closed;
    private LocalDateTime creationDate;
    private LocalDateTime closingDate;
    private String observations;
    private LocalDateTime updateDate;

}