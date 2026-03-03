package org.uteq.resoluciones.resoluciones.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.uteq.resoluciones.resoluciones.dto.CompleteProfileRequest;
import org.uteq.resoluciones.resoluciones.dto.LoginRequest;
import org.uteq.resoluciones.resoluciones.dto.InstanceSimpleResponse;
import org.uteq.resoluciones.resoluciones.dto.LoginResponse;
import org.uteq.resoluciones.resoluciones.dto.MeResponse;
import org.uteq.resoluciones.resoluciones.dto.RegistrationRequest;
import org.uteq.resoluciones.resoluciones.entities.User;
import org.uteq.resoluciones.resoluciones.repository.UserRepository;
import org.uteq.resoluciones.resoluciones.security.JwtService;
import org.uteq.resoluciones.resoluciones.service.RegistrationService;
import org.uteq.resoluciones.resoluciones.service.SecurityService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepo;
    private final SecurityService securityService;
    private final RegistrationService registrationService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos");
        }

        User u = userRepo.findByUser(req.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos"));

        UserDetails ud = org.springframework.security.core.userdetails.User
                .withUsername(u.getUser())
                .password(u.getPassword())
                .authorities("ROLE_USER")
                .build();

        String token = jwtService.generateToken(ud);

        return LoginResponse.builder()
                .id(u.getId())
                .username(u.getUser())
                .names(u.getNames())
                .surnames(u.getSurnames())
                .token(token)
                .profileCompleted(Boolean.TRUE.equals(u.getProfileCompleted()))
                .build();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> register(@Valid @RequestBody RegistrationRequest req) {
        try {
            registrationService.register(req);
            return Map.of("message",
                    "Registro exitoso. Revisa tu correo " + req.getEmail() + " para obtener tus credenciales.");
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PutMapping("/complete-profile")
    public Map<String, String> completeProfile(@Valid @RequestBody CompleteProfileRequest req,
                                               Principal principal) {
        String username = principal.getName();

        User user = userRepo.findByUser(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        user.setNames(req.getNames().trim());
        user.setSurnames(req.getSurnames().trim());
        user.setPhone(req.getPhone() != null ? req.getPhone().trim() : null);

        if (req.getNewPassword() != null && !req.getNewPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        }

        user.setProfileCompleted(true);
        user.setUpdateDate(LocalDateTime.now());
        userRepo.save(user);

        return Map.of("message", "Perfil actualizado exitosamente");
    }

    @GetMapping("/me")
    public MeResponse me(@RequestParam Long userId, @RequestParam Long instanceId) {
        return securityService.me(userId, instanceId);
    }

    @GetMapping("/my-instances")
    public List<InstanceSimpleResponse> myInstances(@RequestParam Long userId) {
        return securityService.getUserInstances(userId);
    }
}
