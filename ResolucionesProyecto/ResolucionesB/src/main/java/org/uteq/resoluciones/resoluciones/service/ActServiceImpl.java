package org.uteq.resoluciones.resoluciones.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.resoluciones.resoluciones.config.ActMapper;
import org.uteq.resoluciones.resoluciones.dto.ActCreateRequest;
import org.uteq.resoluciones.resoluciones.dto.ActResponse;
import org.uteq.resoluciones.resoluciones.entities.Act;
import org.uteq.resoluciones.resoluciones.entities.Session;
import org.uteq.resoluciones.resoluciones.repository.ActRepository;
import org.uteq.resoluciones.resoluciones.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ActServiceImpl implements ActService {

    private final ActRepository actRepo;
    private final SessionRepository sessionRepo;

    @Override
    @Transactional(readOnly = true)
    public List<ActResponse> listAll() {
        return actRepo.findAll().stream().map(ActMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActResponse> listBySession(Long sessionId) {
        return actRepo.findBySession1_Id(sessionId).stream().map(ActMapper::toDto).toList();
    }

    @Override
    public ActResponse create(ActCreateRequest req) {
        Session session = sessionRepo.findById(req.getSessionId())
                .orElseThrow(() -> new EntityNotFoundException("Sesión no encontrada: " + req.getSessionId()));

        String code = generateActNumber(session);

        Act a = new Act();
        a.setSession1(session);
        a.setNumberAct(code);
        a.setClosed(req.getClosed() != null ? req.getClosed() : false);
        a.setCreationDate(LocalDateTime.now());
        a.setObservations(req.getObservations());
        a.setUpdateDate(null);

        a = actRepo.save(a);
        return ActMapper.toDto(a);
    }

    /**
     * Genera un codigo de acta basado en la sesion.
     * Ej: Session "CA-001" -> Acta "ACT-CA-001-01"
     */
    private String generateActNumber(Session session) {
        String sessionCode = session.getSessionNumber() != null ? session.getSessionNumber() : "SES";
        long count = actRepo.countBySession1_Id(session.getId());
        return String.format("ACT-%s-%02d", sessionCode, count + 1);
    }

    @Override
    public ActResponse close(Long actId) {
        Act a = actRepo.findById(actId)
                .orElseThrow(() -> new EntityNotFoundException("Acta no encontrada: " + actId));

        a.setClosed(true);
        a.setClosingDate(LocalDateTime.now());
        a.setUpdateDate(LocalDateTime.now());

        return ActMapper.toDto(actRepo.save(a));
    }
}