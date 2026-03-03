package org.uteq.resoluciones.resoluciones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstanceSimpleResponse {
    private Long id;
    private String name;
}
