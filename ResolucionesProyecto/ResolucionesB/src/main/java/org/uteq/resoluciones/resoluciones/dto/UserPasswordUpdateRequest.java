package org.uteq.resoluciones.resoluciones.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserPasswordUpdateRequest {
    @NotBlank
    private String password;
}
