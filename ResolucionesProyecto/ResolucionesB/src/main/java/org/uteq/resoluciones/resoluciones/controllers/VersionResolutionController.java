package org.uteq.resoluciones.resoluciones.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.uteq.resoluciones.resoluciones.dto.VersionResolutionResponse;
import org.uteq.resoluciones.resoluciones.service.VersionResolutionService;

import java.util.List;

@RestController
@RequestMapping("/api/resolutions/{resolutionId}/versions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VersionResolutionController {

    private final VersionResolutionService service;

    @GetMapping
    public List<VersionResolutionResponse> list(@PathVariable Long resolutionId) {
        return service.listByResolution(resolutionId);
    }

    @PostMapping("/{versionId}/revert")
    public VersionResolutionResponse revert(
            @PathVariable Long resolutionId,
            @PathVariable Long versionId,
            @RequestParam Long userId
    ) {
        return service.revert(resolutionId, versionId, userId);
    }
}
