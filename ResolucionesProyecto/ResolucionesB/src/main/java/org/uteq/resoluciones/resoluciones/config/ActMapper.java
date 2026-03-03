package org.uteq.resoluciones.resoluciones.config;

import org.uteq.resoluciones.resoluciones.dto.ActResponse;
import org.uteq.resoluciones.resoluciones.entities.Act;

public class ActMapper {
    private ActMapper() {}

    public static ActResponse toDto(Act a) {
        return ActResponse.builder()
                .id(a.getId())
                .sessionId(a.getSession1() != null ? a.getSession1().getId() : null)
                .sessionNumber(a.getSession1() != null ? a.getSession1().getSessionNumber() : null)
                .numberAct(a.getNumberAct())
                .closed(a.getClosed())
                .creationDate(a.getCreationDate())
                .closingDate(a.getClosingDate())
                .observations(a.getObservations())
                .updateDate(a.getUpdateDate())
                .build();
    }
}