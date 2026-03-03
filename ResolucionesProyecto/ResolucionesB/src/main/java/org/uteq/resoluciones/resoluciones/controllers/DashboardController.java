package org.uteq.resoluciones.resoluciones.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.uteq.resoluciones.resoluciones.dto.DashboardStats;
import org.uteq.resoluciones.resoluciones.repository.ResolutionRepository;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final ResolutionRepository resolutionRepo;

    @GetMapping("/stats")
    public DashboardStats getStats() {
        long total = resolutionRepo.countByRemovedFalse();
        long inProgress = resolutionRepo.countByRemovedFalseAndState_NameIgnoreCase("EN_ELABORACION");
        long approved = resolutionRepo.countByRemovedFalseAndState_NameIgnoreCase("APROBADO");
        long rejected = resolutionRepo.countByRemovedFalseAndState_NameIgnoreCase("RECHAZADO");
        long transferred = resolutionRepo.countByRemovedFalseAndState_NameIgnoreCase("TRASLADADO");
        long archived = resolutionRepo.countByRemovedFalseAndState_NameIgnoreCase("ARCHIVADO");

        return DashboardStats.builder()
                .totalResolutions(total)
                .inProgress(inProgress)
                .approved(approved)
                .rejected(rejected)
                .transferred(transferred)
                .archived(archived)
                .build();
    }
}
