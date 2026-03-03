package org.uteq.resoluciones.resoluciones.config;

import org.uteq.resoluciones.resoluciones.dto.SessionResponse;
import org.uteq.resoluciones.resoluciones.entities.Session;

public class SessionMapper {
    private SessionMapper() {}

    public static SessionResponse toDto(Session s) {
        return SessionResponse.builder()
                .id(s.getId())
                .instanceId(s.getInstance1() != null ? s.getInstance1().getId() : null)
                .instanceName(s.getInstance1() != null ? s.getInstance1().getName() : null)
                .date(s.getDate())
                .sessionNumber(s.getSessionNumber())
                .tip(s.getTip())
                .closed(s.getClosed())
                .observations(s.getObservations())
                .creationDate(s.getCreationDate())
                .build();
    }
}