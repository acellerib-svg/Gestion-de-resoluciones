package org.uteq.resoluciones.resoluciones.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.resoluciones.resoluciones.dto.*;
import org.uteq.resoluciones.resoluciones.entities.*;
import org.uteq.resoluciones.resoluciones.config.ResolutionMapper;
import org.uteq.resoluciones.resoluciones.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ResolutionServiceImpl implements ResolutionService {

    private final ResolutionRepository resolutionRepo;
    private final ActRepository actRepo;
    private final InstanceRepository instanceRepo;
    private final StateResolutionRepository stateRepo;
    private final UserRepository userRepo;
    private final HistoryResolutionRepository historyRepo;
    private final BinnacleRepository binnacleRepo;
    private final NotificationRepository notificationRepo;
    private final VerssionResolutionRepository versionRepo;
    private final ResolutionStateMachine stateMachine;

    @Override
    public ResolutionResponse create(ResolutionCreateRequest req) {
        Act act1 = actRepo.findById(req.getActId())
                .orElseThrow(() -> new EntityNotFoundException("Acta no encontrada: " + req.getActId()));
        Instance inst = instanceRepo.findById(req.getCurrentInstanceId())
                .orElseThrow(() -> new EntityNotFoundException("Instancia no encontrada: " + req.getCurrentInstanceId()));

        // Estado por defecto: EN_ELABORACION
        StateResolution state = stateRepo.findByNameIgnoreCase("EN_ELABORACION")
                .orElseThrow(() -> new EntityNotFoundException("Estado EN_ELABORACION no encontrado"));

        User createdBy = null;
        if (req.getCreatedByUserId() != null) {
            createdBy = userRepo.findById(req.getCreatedByUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + req.getCreatedByUserId()));
        }

        String code = generateResolutionNumber(inst);

        Resolution r = new Resolution();
        r.setAct1(act1);
        r.setResolutionNumber(code);
        r.setAntecedent(req.getAntecedent());
        r.setResolution(req.getResolution());
        r.setFundament(req.getFundament());
        r.setCurrentInstance(inst);
        r.setState(state);
        r.setTopic(req.getTopic());
        r.setCreation_date(LocalDateTime.now());
        r.setCreatedBy(createdBy);
        r.setRemoved(false);
        r = resolutionRepo.save(r);

        recordHistory(r, inst, null, "CREATED", "Resolucion creada", createdBy);
        recordBinnacle(createdBy, r, "Creo resolucion " + r.getResolutionNumber());

        return ResolutionMapper.toDetailDto(r);
    }

    /**
     * Genera un codigo de resolucion basado en la instancia.
     * Formato: RES-[ABBR]-[SEQ] ej: RES-CA-001
     */
    private String generateResolutionNumber(Instance inst) {
        String abbr = buildInstanceAbbreviation(inst.getName());
        long count = resolutionRepo.countByCurrentInstanceId(inst.getId());
        return String.format("RES-%s-%03d", abbr, count + 1);
    }

    private String buildInstanceAbbreviation(String name) {
        if (name == null || name.isBlank()) return "GEN";
        String[] words = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (!w.isEmpty()) {
                String lower = w.toLowerCase();
                if (lower.equals("de") || lower.equals("del") || lower.equals("la")
                        || lower.equals("el") || lower.equals("las") || lower.equals("los")
                        || lower.equals("y") || lower.equals("e")) {
                    continue;
                }
                sb.append(Character.toUpperCase(w.charAt(0)));
            }
        }
        return sb.length() > 0 ? sb.toString() : "GEN";
    }

    @Override
    @Transactional(readOnly = true)
    public ResolutionResponse getById(Long resolutionId) {
        Resolution r = findResolution(resolutionId);
        return ResolutionMapper.toDetailDto(r);
    }

    @Override
    public ResolutionResponse update(Long resolutionId, ResolutionUpdateRequest req) {
        Resolution r = findResolution(resolutionId);
        User user = req.getUserId() != null ? findUser(req.getUserId()) : null;

        // Create version snapshot before updating
        createVersionSnapshot(r, user, "Actualizacion de contenido");

        if (req.getAntecedent() != null) r.setAntecedent(req.getAntecedent());
        if (req.getResolution() != null) r.setResolution(req.getResolution());
        if (req.getFundament() != null) r.setFundament(req.getFundament());
        if (req.getTopic() != null) r.setTopic(req.getTopic());
        r.setUpdateDate(LocalDateTime.now());
        r.setUpdateBy(user);
        resolutionRepo.save(r);

        recordBinnacle(user, r, "Actualizo contenido de resolucion #" + r.getResolutionNumber());
        return ResolutionMapper.toDetailDto(r);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResolutionResponse> list(Long stateId, Long instanceId, Long actId, Pageable pageable) {
        Page<Resolution> page;
        if (stateId != null) {
            page = resolutionRepo.findByRemovedFalseAndState_StateResolution(stateId, pageable);
        } else if (instanceId != null) {
            page = resolutionRepo.findByRemovedFalseAndCurrentInstanceId(instanceId, pageable);
        } else if (actId != null) {
            page = resolutionRepo.findByRemovedFalseAndAct1Id(actId, pageable);
        } else {
            page = resolutionRepo.findByRemovedFalse(pageable);
        }
        return page.map(ResolutionMapper::toDto);
    }

    @Override
    public ResolutionResponse approve(Long resolutionId, ResolutionActionRequest req) {
        return changeState(resolutionId, "APROBADO", req, "APPROVED", "Resolucion aprobada");
    }

    @Override
    public ResolutionResponse reject(Long resolutionId, ResolutionActionRequest req) {
        return changeState(resolutionId, "RECHAZADO", req, "REJECTED", "Resolucion rechazada");
    }

    @Override
    public ResolutionResponse transfer(Long resolutionId, ResolutionActionRequest req) {
        Resolution r = findResolution(resolutionId);
        User user = findUser(req.getUserId());

        String currentStateName = r.getState().getName().toUpperCase();
        stateMachine.validateTransition(currentStateName, "TRASLADADO");

        if (req.getDestinationInstanceId() == null) {
            throw new IllegalArgumentException("Debe especificar la instancia destino para trasladar");
        }

        Instance dest = instanceRepo.findById(req.getDestinationInstanceId())
                .orElseThrow(() -> new EntityNotFoundException("Instancia destino no encontrada"));

        Instance origin = r.getCurrentInstance();

        StateResolution trasladado = stateRepo.findByNameIgnoreCase("TRASLADADO")
                .orElseThrow(() -> new EntityNotFoundException("Estado TRASLADADO no encontrado"));

        r.setState(trasladado);
        r.setCurrentInstance(dest);
        r.setUpdateDate(LocalDateTime.now());
        r.setUpdateBy(user);
        resolutionRepo.save(r);

        recordHistory(r, origin, dest, "TRANSFERRED",
                req.getObservations() != null ? req.getObservations() : "Trasladada a " + dest.getName(), user);
        recordBinnacle(user, r, "Traslado resolucion #" + r.getResolutionNumber() + " a " + dest.getName());
        createNotification(user, "Se traslado la resolucion #" + r.getResolutionNumber() + " a " + dest.getName());

        return ResolutionMapper.toDetailDto(r);
    }

    @Override
    public ResolutionResponse archive(Long resolutionId, ResolutionActionRequest req) {
        return changeState(resolutionId, "ARCHIVADO", req, "ARCHIVED", "Resolucion archivada");
    }

    @Override
    public ResolutionResponse reopen(Long resolutionId, ResolutionActionRequest req) {
        return changeState(resolutionId, "EN_ELABORACION", req, "REOPENED", "Resolucion reabierta para revision");
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoryResponse> history(Long resolutionId) {
        List<HistoryResolution> list = historyRepo.findByResolution3IdOrderByDateAsc(resolutionId);
        return list.stream().map(h ->
                HistoryResponse.builder()
                        .id(h.getId())
                        .action(h.getAction())
                        .date(h.getDate())
                        .observations(h.getObservations())
                        .instanceOriginId(h.getInstance1() != null ? h.getInstance1().getId() : null)
                        .instanceOriginName(h.getInstance1() != null ? h.getInstance1().getName() : null)
                        .instanceDestinationId(h.getInstanceDestination() != null ? h.getInstanceDestination().getId() : null)
                        .instanceDestinationName(h.getInstanceDestination() != null ? h.getInstanceDestination().getName() : null)
                        .userId(h.getUser3() != null ? h.getUser3().getId() : null)
                        .username(h.getUser3() != null ? h.getUser3().getUser() : null)
                        .build()
        ).toList();
    }

    @Override
    public void softDelete(Long resolutionId, Long userId) {
        Resolution r = findResolution(resolutionId);
        User u = userId != null ? findUser(userId) : null;
        r.setRemoved(true);
        r.setUpdateDate(LocalDateTime.now());
        r.setUpdateBy(u);
        resolutionRepo.save(r);
        recordBinnacle(u, r, "Elimino (soft delete) la resolucion #" + r.getResolutionNumber());
    }

    // ---- Private helpers ----

    private ResolutionResponse changeState(Long resolutionId, String targetStateName,
                                           ResolutionActionRequest req, String historyAction, String defaultObs) {
        Resolution r = findResolution(resolutionId);
        User user = findUser(req.getUserId());

        String currentStateName = r.getState().getName().toUpperCase();
        stateMachine.validateTransition(currentStateName, targetStateName);

        StateResolution targetState = stateRepo.findByNameIgnoreCase(targetStateName)
                .orElseThrow(() -> new EntityNotFoundException("Estado " + targetStateName + " no encontrado"));

        r.setState(targetState);
        r.setUpdateDate(LocalDateTime.now());
        r.setUpdateBy(user);
        resolutionRepo.save(r);

        String obs = req.getObservations() != null ? req.getObservations() : defaultObs;
        recordHistory(r, r.getCurrentInstance(), null, historyAction, obs, user);
        recordBinnacle(user, r, historyAction + " resolucion #" + r.getResolutionNumber());
        createNotification(user, "Resolucion #" + r.getResolutionNumber() + ": " + defaultObs);

        return ResolutionMapper.toDetailDto(r);
    }

    private void recordHistory(Resolution r, Instance origin, Instance dest, String action, String obs, User user) {
        HistoryResolution h = new HistoryResolution();
        h.setResolution3(r);
        h.setInstance1(origin);
        h.setInstanceDestination(dest);
        h.setAction(action);
        h.setDate(LocalDateTime.now());
        h.setObservations(obs);
        h.setUser3(user);
        historyRepo.save(h);
    }

    private void recordBinnacle(User user, Resolution r, String action) {
        Binnacle b = new Binnacle();
        b.setUser2(user);
        b.setResolution3(r);
        b.setTipAction1(null);
        b.setAction(action);
        b.setDate(LocalDateTime.now());
        binnacleRepo.save(b);
    }

    private void createNotification(User user, String message) {
        Notification n = new Notification();
        n.setUser4(user);
        n.setMessage(message);
        n.setDate(LocalDateTime.now());
        n.setState(false);
        n.setChannel("SYSTEM");
        notificationRepo.save(n);
    }

    private void createVersionSnapshot(Resolution r, User user, String reason) {
        Integer nextVersion = versionRepo.findTopByResolution3IdOrderByVersionDesc(r.getId())
                .map(v -> v.getVersion() + 1).orElse(1);

        String content = "{\"antecedent\":\"" + escapeJson(r.getAntecedent()) +
                "\",\"resolution\":\"" + escapeJson(r.getResolution()) +
                "\",\"fundament\":\"" + escapeJson(r.getFundament() != null ? r.getFundament() : "") +
                "\",\"topic\":\"" + escapeJson(r.getTopic() != null ? r.getTopic() : "") + "\"}";

        VersionResolution v = new VersionResolution();
        v.setResolution3(r);
        v.setVersion(nextVersion);
        v.setContent(content);
        v.setDate(LocalDateTime.now());
        v.setUser6(user);
        v.setReason(reason);
        versionRepo.save(v);
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    private Resolution findResolution(Long id) {
        return resolutionRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resolucion no encontrada: " + id));
    }

    private User findUser(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + id));
    }
}
