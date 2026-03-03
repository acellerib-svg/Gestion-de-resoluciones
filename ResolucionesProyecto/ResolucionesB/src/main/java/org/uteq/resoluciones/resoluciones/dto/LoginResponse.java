package org.uteq.resoluciones.resoluciones.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private Long id;
    private String username;
    private String names;
    private String surnames;
    private String token; // ✅
    private boolean profileCompleted;
}