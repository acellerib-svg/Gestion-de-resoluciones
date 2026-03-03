package org.uteq.resoluciones.resoluciones.service;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Set;

@Component
public class ResolutionStateMachine {

    private static final Map<String, Set<String>> TRANSITIONS = Map.of(
        "EN_ELABORACION", Set.of("APROBADO", "RECHAZADO", "TRASLADADO"),
        "APROBADO", Set.of("ARCHIVADO", "TRASLADADO"),
        "RECHAZADO", Set.of("EN_ELABORACION"),
        "TRASLADADO", Set.of("EN_ELABORACION"),
        "ARCHIVADO", Set.of()
    );

    public boolean canTransition(String from, String to) {
        Set<String> allowed = TRANSITIONS.getOrDefault(from.toUpperCase(), Set.of());
        return allowed.contains(to.toUpperCase());
    }

    public void validateTransition(String from, String to) {
        if (!canTransition(from, to)) {
            throw new IllegalStateException(
                "Transicion invalida: de '" + from + "' a '" + to + "'"
            );
        }
    }

    public Set<String> getAllowedTransitions(String currentState) {
        return TRANSITIONS.getOrDefault(currentState.toUpperCase(), Set.of());
    }
}
