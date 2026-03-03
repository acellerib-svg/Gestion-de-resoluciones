package org.uteq.resoluciones.resoluciones.config;

import org.uteq.resoluciones.resoluciones.dto.PermissionResponse;
import org.uteq.resoluciones.resoluciones.entities.Permission;

public class PermissionMapper {
    private PermissionMapper(){}

    public static PermissionResponse toDto(Permission p){
        return PermissionResponse.builder()
                .id(p.getId())
                .code(p.getCode())
                .name(p.getName())
                .description(p.getDescription())
                .module(p.getModule())
                .active(p.getActive())
                .creationDate(p.getCreationDate())
                .updateDate(p.getUpdateDate())
                .build();
    }
}
