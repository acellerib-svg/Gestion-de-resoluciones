package org.uteq.resoluciones.resoluciones.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStats {
    private long totalResolutions;
    private long inProgress;
    private long approved;
    private long rejected;
    private long transferred;
    private long archived;
}
