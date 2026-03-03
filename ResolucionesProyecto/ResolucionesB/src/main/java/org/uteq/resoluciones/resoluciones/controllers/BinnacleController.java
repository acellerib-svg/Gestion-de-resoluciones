package org.uteq.resoluciones.resoluciones.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.uteq.resoluciones.resoluciones.dto.BinnacleResponse;
import org.uteq.resoluciones.resoluciones.service.BinnacleService;

@RestController
@RequestMapping("/api/binnacle")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BinnacleController {

    private final BinnacleService service;

    @GetMapping
    public Page<BinnacleResponse> list(
            @RequestParam(required = false) Long resolutionId,
            @RequestParam(required = false) Long userId,
            Pageable pageable) {
        return service.list(resolutionId, userId, pageable);
    }
}
