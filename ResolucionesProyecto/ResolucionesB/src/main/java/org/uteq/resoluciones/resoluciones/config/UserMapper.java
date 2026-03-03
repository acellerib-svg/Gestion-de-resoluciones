package org.uteq.resoluciones.resoluciones.config;

import org.uteq.resoluciones.resoluciones.dto.UserCreateRequest;
import org.uteq.resoluciones.resoluciones.dto.UserResponse;
import org.uteq.resoluciones.resoluciones.dto.UserUpdateRequest;
import org.uteq.resoluciones.resoluciones.entities.User;

import java.time.LocalDateTime;

public class UserMapper {
    private UserMapper() {}

    // Entity -> Response DTO (lo que devuelves al frontend)
    public static UserResponse toDto(User u) {
        if (u == null) return null;

        return UserResponse.builder()
                .id(u.getId())
                .user(u.getUser())
                .names(u.getNames())
                .surnames(u.getSurnames())
                .phone(u.getPhone())
                .email(u.getEmail())
                .state(u.getState())
                .creationDate(u.getCreationDate())
                .updateDate(u.getUpdateDate())
                .lastAccess(u.getLastAccess())
                .build();
    }

    // CreateRequest -> Entity (para crear nuevo usuario)
    public static User fromCreate(UserCreateRequest req) {
        if (req == null) return null;

        User u = new User();
        u.setUser(req.getUser());
        u.setPassword(req.getPassword()); // luego aquí iría BCrypt
        u.setNames(req.getNames());
        u.setSurnames(req.getSurnames());
        u.setPhone(req.getPhone());
        u.setEmail(req.getEmail());
        u.setState(req.getState());

        // campos de control
        u.setCreationDate(LocalDateTime.now());
        u.setUpdateDate(null);
        u.setLastAccess(null);

        return u;
    }

    // UpdateRequest -> aplica cambios sobre entity existente
    public static void applyUpdate(UserUpdateRequest req, User userDb) {
        if (req == null || userDb == null) return;

        userDb.setUser(req.getUser());
        userDb.setNames(req.getNames());
        userDb.setSurnames(req.getSurnames());
        userDb.setPhone(req.getPhone());
        userDb.setEmail(req.getEmail());
        userDb.setState(req.getState());

        // control
        userDb.setUpdateDate(LocalDateTime.now());
        // lastAccess normalmente NO se actualiza aquí
    }
}
