package org.uteq.resoluciones.resoluciones.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.uteq.resoluciones.resoluciones.dto.*;
import org.uteq.resoluciones.resoluciones.service.ResolutionService;

import java.util.List;

@RestController
@RequestMapping("/api/resolutions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ResolutionController {

    private final ResolutionService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResolutionResponse create(@Valid @RequestBody ResolutionCreateRequest req) {
        return service.create(req);
    }

    @GetMapping("/{id}")
    public ResolutionResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public ResolutionResponse update(@PathVariable Long id, @Valid @RequestBody ResolutionUpdateRequest req) {
        return service.update(id, req);
    }

    @GetMapping
    public Page<ResolutionResponse> list(
            @RequestParam(required = false) Long stateId,
            @RequestParam(required = false) Long instanceId,
            @RequestParam(required = false) Long actId,
            Pageable pageable
    ) {
        return service.list(stateId, instanceId, actId, pageable);
    }

    @PostMapping("/{id}/approve")
    public ResolutionResponse approve(@PathVariable Long id, @Valid @RequestBody ResolutionActionRequest req) {
        return service.approve(id, req);
    }

    @PostMapping("/{id}/reject")
    public ResolutionResponse reject(@PathVariable Long id, @Valid @RequestBody ResolutionActionRequest req) {
        return service.reject(id, req);
    }

    @PostMapping("/{id}/transfer")
    public ResolutionResponse transfer(@PathVariable Long id, @Valid @RequestBody ResolutionActionRequest req) {
        return service.transfer(id, req);
    }

    @PostMapping("/{id}/archive")
    public ResolutionResponse archive(@PathVariable Long id, @Valid @RequestBody ResolutionActionRequest req) {
        return service.archive(id, req);
    }

    @PostMapping("/{id}/reopen")
    public ResolutionResponse reopen(@PathVariable Long id, @Valid @RequestBody ResolutionActionRequest req) {
        return service.reopen(id, req);
    }

    @GetMapping("/{id}/history")
    public List<HistoryResponse> history(@PathVariable Long id) {
        return service.history(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void softDelete(@PathVariable Long id, @RequestParam(required = false) Long userId) {
        service.softDelete(id, userId);
    }
}
