package org.uteq.resoluciones.resoluciones.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SessionResponse {

    private Long id;
    private Long instanceId;
    private String instanceName;
    private LocalDateTime date;
    private String sessionNumber;
    private String tip;
    private Boolean closed;
    private String observations;
    private LocalDateTime creationDate;

}