package org.uteq.resoluciones.resoluciones.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.uteq.resoluciones.resoluciones.config.UserMapper;
import org.uteq.resoluciones.resoluciones.dto.UserCreateRequest;
import org.uteq.resoluciones.resoluciones.dto.UserPasswordUpdateRequest;
import org.uteq.resoluciones.resoluciones.dto.UserResponse;
import org.uteq.resoluciones.resoluciones.dto.UserUpdateRequest;
import org.uteq.resoluciones.resoluciones.entities.User;
import org.uteq.resoluciones.resoluciones.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // CREATE
    @PostMapping
    public UserResponse create(@Valid @RequestBody UserCreateRequest req) {
        User u = UserMapper.fromCreate(req);

        // ✅ bcrypt
        u.setPassword(passwordEncoder.encode(req.getPassword()));

        return UserMapper.toDto(userService.save(u));
    }

    // READ ALL
    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) {
        return UserMapper.toDto(userService.findById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest req) {

        if (req.getId() == null || !req.getId().equals(id)) {
            throw new RuntimeException("El id del path no coincide con el id del body");
        }

        User userDb = userService.findById(id);
        UserMapper.applyUpdate(req, userDb);
        return UserMapper.toDto(userService.save(userDb));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteByid(id);
    }

    // UPDATE PASSWORD
    @PatchMapping("/{id}/password")
    public void updatePassword(@PathVariable Long id, @Valid @RequestBody UserPasswordUpdateRequest req) {
        User userDb = userService.findById(id);

        // ✅ bcrypt
        userDb.setPassword(passwordEncoder.encode(req.getPassword()));
        userDb.setUpdateDate(java.time.LocalDateTime.now());

        userService.save(userDb);
    }
}
