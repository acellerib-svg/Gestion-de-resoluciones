package org.uteq.resoluciones.resoluciones.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResolutionResponse {
    private Long id;
    private String resolutionNumber;
    private String topic;
    private Boolean removed;

    private Long actId;
    private Long currentInstanceId;
    private String currentInstanceName;

    private Long stateId;
    private String stateName;

    private LocalDateTime creationDate;
    private LocalDateTime updateDate;

    // Detail fields
    private String antecedent;
    private String resolution;
    private String fundament;

    private Long createdByUserId;
    private String createdByName;
    private Long updatedByUserId;
    private String updatedByName;
}
