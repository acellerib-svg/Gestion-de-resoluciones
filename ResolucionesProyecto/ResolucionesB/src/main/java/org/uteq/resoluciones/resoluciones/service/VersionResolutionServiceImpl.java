package org.uteq.resoluciones.resoluciones.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.resoluciones.resoluciones.dto.VersionResolutionResponse;
import org.uteq.resoluciones.resoluciones.entities.*;
import org.uteq.resoluciones.resoluciones.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VersionResolutionServiceImpl implements VersionResolutionService {

    private final VerssionResolutionRepository versionRepo;
    private final ResolutionRepository resolutionRepo;
    private final UserRepository userRepo;
    private final HistoryResolutionRepository historyRepo;
    private final BinnacleRepository binnacleRepo;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional(readOnly = true)
    public List<VersionResolutionResponse> listByResolution(Long resolutionId) {
        return versionRepo.findByResolution3IdOrderByVersionDesc(resolutionId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public VersionResolutionResponse revert(Long resolutionId, Long versionId, Long userId) {
        Resolution r = resolutionRepo.findById(resolutionId)
                .orElseThrow(() -> new EntityNotFoundException("Resolucion no encontrada: " + resolutionId));

        VersionResolution ver = versionRepo.findById(versionId)
                .orElseThrow(() -> new EntityNotFoundException("Version no encontrada: " + versionId));

        if (!ver.getResolution3().getId().equals(resolutionId)) {
            throw new IllegalArgumentException("La version no pertenece a esta resolucion");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + userId));

        // Create snapshot of current state before reverting
        Integer nextVersion = versionRepo.findTopByResolution3IdOrderByVersionDesc(resolutionId)
                .map(v -> v.getVersion() + 1).orElse(1);

        String currentContent = buildContentJson(r);
        VersionResolution snapshot = new VersionResolution();
        snapshot.setResolution3(r);
        snapshot.setVersion(nextVersion);
        snapshot.setContent(currentContent);
        snapshot.setDate(LocalDateTime.now());
        snapshot.setUser6(user);
        snapshot.setReason("Snapshot antes de revertir a version " + ver.getVersion());
        versionRepo.save(snapshot);

        // Revert resolution to the selected version
        try {
            JsonNode node = objectMapper.readTree(ver.getContent());
            if (node.has("antecedent")) r.setAntecedent(node.get("antecedent").asText());
            if (node.has("resolution")) r.setResolution(node.get("resolution").asText());
            if (node.has("fundament")) r.setFundament(node.get("fundament").asText());
            if (node.has("topic")) r.setTopic(node.get("topic").asText());
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear el contenido de la version", e);
        }

        r.setUpdateDate(LocalDateTime.now());
        r.setUpdateBy(user);
        resolutionRepo.save(r);

        // Record history and binnacle
        HistoryResolution h = new HistoryResolution();
        h.setResolution3(r);
        h.setInstance1(r.getCurrentInstance());
        h.setAction("REVERTED");
        h.setDate(LocalDateTime.now());
        h.setObservations("Revertida a version " + ver.getVersion());
        h.setUser3(user);
        historyRepo.save(h);

        Binnacle b = new Binnacle();
        b.setUser2(user);
        b.setResolution3(r);
        b.setAction("Revirtio resolucion #" + r.getResolutionNumber() + " a version " + ver.getVersion());
        b.setDate(LocalDateTime.now());
        binnacleRepo.save(b);

        return toDto(ver);
    }

    private String buildContentJson(Resolution r) {
        String ant = escape(r.getAntecedent());
        String res = escape(r.getResolution());
        String fund = escape(r.getFundament() != null ? r.getFundament() : "");
        String topic = escape(r.getTopic() != null ? r.getTopic() : "");
        return "{\"antecedent\":\"" + ant + "\",\"resolution\":\"" + res +
                "\",\"fundament\":\"" + fund + "\",\"topic\":\"" + topic + "\"}";
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    private VersionResolutionResponse toDto(VersionResolution v) {
        return VersionResolutionResponse.builder()
                .id(v.getId())
                .version(v.getVersion())
                .content(v.getContent())
                .date(v.getDate())
                .userId(v.getUser6() != null ? v.getUser6().getId() : null)
                .userName(v.getUser6() != null ? v.getUser6().getUser() : null)
                .reason(v.getReason())
                .build();
    }
}
