package org.uteq.resoluciones.resoluciones.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String user;
    private String names;
    private String surnames;
    private String phone;
    private String email;
    private Boolean state;

    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private LocalDateTime lastAccess;
}