package org.uteq.resoluciones.resoluciones.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegistrationRequest {

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Correo no válido")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@uteq\\.edu\\.ec$",
             message = "Solo se permiten correos institucionales @uteq.edu.ec")
    private String email;
}
