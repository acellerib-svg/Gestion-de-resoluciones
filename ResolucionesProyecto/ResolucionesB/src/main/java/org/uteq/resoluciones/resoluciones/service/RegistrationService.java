package org.uteq.resoluciones.resoluciones.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.resoluciones.resoluciones.dto.RegistrationRequest;
import org.uteq.resoluciones.resoluciones.entities.User;
import org.uteq.resoluciones.resoluciones.repository.UserRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Transactional
    public void register(RegistrationRequest req) {

        String email = req.getEmail().trim().toLowerCase();

        if (userRepo.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Ya existe una cuenta registrada con este correo.");
        }

        // Generar usuario a partir del correo (parte antes de @)
        // Ej: "pedrito@uteq.edu.ec" -> usuario "pedrito"
        String baseUsername = email.substring(0, email.indexOf('@'))
                .replaceAll("[^a-zA-Z0-9._-]", "");

        String username = baseUsername;
        int suffix = 1;
        while (userRepo.findByUser(username).isPresent()) {
            username = baseUsername + suffix;
            suffix++;
        }

        // Generar contraseña aleatoria de 10 caracteres
        String rawPassword = generatePassword(10);

        User user = new User();
        user.setUser(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setNames(baseUsername);
        user.setSurnames(null);
        user.setPhone(null);
        user.setEmail(email);
        user.setState(true);
        user.setProfileCompleted(false);
        user.setCreationDate(LocalDateTime.now());

        userRepo.save(user);

        sendCredentialsEmail(email, username, rawPassword);
    }

    private String generatePassword(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    private void sendCredentialsEmail(String to, String username, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Sistema de Resoluciones UTEQ - Tus credenciales de acceso");
        message.setText(
                "Hola,\n\n" +
                "Tu cuenta en el Sistema de Resoluciones ha sido creada exitosamente.\n\n" +
                "Tus credenciales de acceso son:\n\n" +
                "   Usuario:      " + username + "\n" +
                "   Contraseña:   " + password + "\n\n" +
                "Te recomendamos cambiar tu contraseña después del primer inicio de sesión.\n\n" +
                "Saludos,\n" +
                "Sistema de Resoluciones - UTEQ"
        );

        mailSender.send(message);
    }
}
