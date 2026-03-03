package org.uteq.resoluciones.resoluciones.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateRequest {

    @NotNull
    private Long id;

    @NotBlank
    private String user;

    @NotBlank
    private String names;

    @NotBlank
    private String surnames;

    private String phone;

    @Email
    @NotBlank
    private String email;

    @NotNull
    private Boolean state;
}
