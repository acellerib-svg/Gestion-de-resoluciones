package org.uteq.resoluciones.resoluciones.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class InstanceResponse {

    private Long id;
    private String name;

    private Long levelId;
    private String levelName;

    private Long majorId;
    private String majorName;

    private Long instanceFatherId;
    private String instanceFatherName;

    private String description;
    private Boolean active;

    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
}
