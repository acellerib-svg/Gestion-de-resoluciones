package org.uteq.resoluciones.resoluciones.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_history_resolution")
public class HistoryResolution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_history")
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name = "id_resolution", nullable = false,
            foreignKey = @ForeignKey(name = "fk_history_resolution"))
    private Resolution resolution3;

    @ManyToOne(optional=false)
    @JoinColumn(name = "id_instance", nullable = false,
            foreignKey = @ForeignKey(name = "fk_history_resolution_instance"))
    private Instance instance1;

    @ManyToOne
    @JoinColumn(name = "id_instance_destination",
            foreignKey = @ForeignKey(name = "fk_history_instance_destination"))
    private Instance instanceDestination;

    @Column(name = "action", nullable = false, length = 100)
    private String action;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "observations", length = 500)
    private String observations;

    @ManyToOne
    @JoinColumn(name = "id_user", foreignKey = @ForeignKey(name = "fk_history_user"))
    private User user3;
}
