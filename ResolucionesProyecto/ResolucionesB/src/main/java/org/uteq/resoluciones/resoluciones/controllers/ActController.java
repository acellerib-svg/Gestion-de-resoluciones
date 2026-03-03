package org.uteq.resoluciones.resoluciones.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.uteq.resoluciones.resoluciones.dto.ActCreateRequest;
import org.uteq.resoluciones.resoluciones.dto.ActResponse;
import org.uteq.resoluciones.resoluciones.dto.ResolutionResponse;
import org.uteq.resoluciones.resoluciones.service.ActService;
import org.uteq.resoluciones.resoluciones.service.ResolutionService;

import java.util.List;

@RestController
@RequestMapping("/api/acts")
@RequiredArgsConstructor
public class ActController {

    private final ActService service;
    private final ResolutionService resolutionService;

    @GetMapping
    public List<ActResponse> list(@RequestParam(required = false) Long sessionId) {
        if (sessionId != null) {
            return service.listBySession(sessionId);
        }
        return service.listAll();
    }

    @PostMapping
    public ActResponse create(@Valid @RequestBody ActCreateRequest req) {
        return service.create(req);
    }

    @PatchMapping("/{id}/close")
    public ActResponse close(@PathVariable Long id) {
        return service.close(id);
    }

    @GetMapping("/{id}/resolutions")
    public Page<ResolutionResponse> resolutionsByAct(@PathVariable Long id, Pageable pageable) {
        return resolutionService.list(null, null, id, pageable);
    }
}