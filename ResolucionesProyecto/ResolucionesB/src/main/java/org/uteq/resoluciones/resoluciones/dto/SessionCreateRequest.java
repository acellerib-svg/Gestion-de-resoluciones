package org.uteq.resoluciones.resoluciones.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data

public class SessionCreateRequest {

    @NotNull private Long instanceId;
    @NotNull private LocalDateTime date;
    @NotNull private String tip;
    @NotNull private Boolean closed;
    private String observations;

}
