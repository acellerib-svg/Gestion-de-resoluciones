package org.uteq.resoluciones.resoluciones.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.resoluciones.resoluciones.config.SessionMapper;
import org.uteq.resoluciones.resoluciones.dto.SessionCreateRequest;
import org.uteq.resoluciones.resoluciones.dto.SessionResponse;
import org.uteq.resoluciones.resoluciones.entities.Instance;
import org.uteq.resoluciones.resoluciones.entities.Session;
import org.uteq.resoluciones.resoluciones.repository.InstanceRepository;
import org.uteq.resoluciones.resoluciones.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepo;
    private final InstanceRepository instanceRepo;

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponse> list(Long instanceId) {
        List<Session> list = (instanceId != null)
                ? sessionRepo.findByInstance1_Id(instanceId)
                : sessionRepo.findAll();

        return list.stream().map(SessionMapper::toDto).toList();
    }

    @Override
    public SessionResponse create(SessionCreateRequest req) {
        Instance inst = instanceRepo.findById(req.getInstanceId())
                .orElseThrow(() -> new EntityNotFoundException("Instancia no encontrada: " + req.getInstanceId()));

        String code = generateSessionNumber(inst);

        Session s = new Session();
        s.setInstance1(inst);
        s.setDate(req.getDate());
        s.setSessionNumber(code);
        s.setTip(req.getTip());
        s.setClosed(req.getClosed());
        s.setObservations(req.getObservations());
        s.setCreationDate(LocalDateTime.now());

        s = sessionRepo.save(s);
        return SessionMapper.toDto(s);
    }

    /**
     * Genera un codigo de sesion basado en la abreviatura de la instancia.
     * Ej: "Consejo Academico" -> "CA-001", "Consejo Directivo" -> "CD-002"
     */
    private String generateSessionNumber(Instance inst) {
        String abbr = buildAbbreviation(inst.getName());
        long count = sessionRepo.countByInstance1_Id(inst.getId());
        return String.format("%s-%03d", abbr, count + 1);
    }

    /**
     * Construye una abreviatura a partir del nombre de la instancia
     * tomando la primera letra de cada palabra en mayuscula.
     * "Consejo Academico" -> "CA"
     * "Consejo Directivo de Carrera" -> "CDC"
     */
    private String buildAbbreviation(String name) {
        if (name == null || name.isBlank()) return "SES";
        String[] words = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (!w.isEmpty()) {
                // Ignorar articulos y preposiciones cortas
                String lower = w.toLowerCase();
                if (lower.equals("de") || lower.equals("del") || lower.equals("la")
                        || lower.equals("el") || lower.equals("las") || lower.equals("los")
                        || lower.equals("y") || lower.equals("e")) {
                    continue;
                }
                sb.append(Character.toUpperCase(w.charAt(0)));
            }
        }
        return sb.length() > 0 ? sb.toString() : "SES";
    }
}