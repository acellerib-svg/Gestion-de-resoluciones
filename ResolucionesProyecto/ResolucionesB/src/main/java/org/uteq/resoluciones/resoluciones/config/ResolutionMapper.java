package org.uteq.resoluciones.resoluciones.config;

import org.uteq.resoluciones.resoluciones.dto.ResolutionResponse;
import org.uteq.resoluciones.resoluciones.entities.Resolution;

public class ResolutionMapper {

    private ResolutionMapper(){}

    public static ResolutionResponse toDto(Resolution r) {
        return ResolutionResponse.builder()
                .id(r.getId())
                .resolutionNumber(r.getResolutionNumber())
                .topic(r.getTopic())
                .removed(r.getRemoved())
                .actId(r.getAct1() != null ? r.getAct1().getId() : null)
                .currentInstanceId(r.getCurrentInstance() != null ? r.getCurrentInstance().getId() : null)
                .currentInstanceName(r.getCurrentInstance() != null ? r.getCurrentInstance().getName() : null)
                .stateId(r.getState() != null ? r.getState().getStateResolution() : null)
                .stateName(r.getState() != null ? r.getState().getName() : null)
                .creationDate(r.getCreation_date())
                .updateDate(r.getUpdateDate())
                .build();
    }

    public static ResolutionResponse toDetailDto(Resolution r) {
        return ResolutionResponse.builder()
                .id(r.getId())
                .resolutionNumber(r.getResolutionNumber())
                .topic(r.getTopic())
                .removed(r.getRemoved())
                .actId(r.getAct1() != null ? r.getAct1().getId() : null)
                .currentInstanceId(r.getCurrentInstance() != null ? r.getCurrentInstance().getId() : null)
                .currentInstanceName(r.getCurrentInstance() != null ? r.getCurrentInstance().getName() : null)
                .stateId(r.getState() != null ? r.getState().getStateResolution() : null)
                .stateName(r.getState() != null ? r.getState().getName() : null)
                .creationDate(r.getCreation_date())
                .updateDate(r.getUpdateDate())
                .antecedent(r.getAntecedent())
                .resolution(r.getResolution())
                .fundament(r.getFundament())
                .createdByUserId(r.getCreatedBy() != null ? r.getCreatedBy().getId() : null)
                .createdByName(r.getCreatedBy() != null ? formatName(r.getCreatedBy()) : null)
                .updatedByUserId(r.getUpdateBy() != null ? r.getUpdateBy().getId() : null)
                .updatedByName(r.getUpdateBy() != null ? formatName(r.getUpdateBy()) : null)
                .build();
    }

    private static String formatName(org.uteq.resoluciones.resoluciones.entities.User u) {
        String full = ((u.getNames() != null ? u.getNames() : "") + " " + (u.getSurnames() != null ? u.getSurnames() : "")).trim();
        return full.isEmpty() ? u.getUser() : full;
    }
}
