package org.uteq.resoluciones.resoluciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompleteProfileRequest {

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 150)
    private String names;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 150)
    private String surnames;

    @Size(max = 30)
    private String phone;

    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String newPassword;
}
