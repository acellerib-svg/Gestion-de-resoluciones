package org.uteq.resoluciones.resoluciones.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.uteq.resoluciones.resoluciones.dto.SessionCreateRequest;
import org.uteq.resoluciones.resoluciones.dto.SessionResponse;
import org.uteq.resoluciones.resoluciones.service.SessionService;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService service;

    @GetMapping
    public List<SessionResponse> list(@RequestParam(required=false) Long instanceId) {
        return service.list(instanceId);
    }

    @PostMapping
    public SessionResponse create(@Valid @RequestBody SessionCreateRequest req) {
        return service.create(req);
    }
}