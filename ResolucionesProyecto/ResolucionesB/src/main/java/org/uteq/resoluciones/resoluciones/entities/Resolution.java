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
@Table(name = "tb_resolution")
public class Resolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resolution")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_act", nullable = false, foreignKey = @ForeignKey(name = "fk_resolution_acta1"))
    private Act act1;

    @Column(name = "resolution_number", nullable = false, length = 40)
    private String resolutionNumber;

    @Lob
    @Column(name = "antecedent", nullable = false, columnDefinition = "text")
    private String antecedent;

    @Lob
    @Column(name = "resolution", nullable = false, columnDefinition = "text")
    private String resolution;

    @Lob
    @Column(name = "fundament", columnDefinition = "text")
    private String fundament;

    @ManyToOne
    @JoinColumn(name = "id_current_instance", nullable = false, foreignKey = @ForeignKey(name = "fk_resolution_current_instance"))
    private Instance currentInstance;

    @Column(name = "creation_date")
    private LocalDateTime creation_date;

    @Column(name = "topic", length = 200)
    private String topic;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @ManyToOne(optional = true)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_resolution_created_by"))
    private User createdBy;

    @ManyToOne(optional = true)
    @JoinColumn(name = "update_by", foreignKey = @ForeignKey(name = "fk_resolution_update_by"))
    private User updateBy;

    @Column(name = "removed", nullable = false)
    private Boolean removed;

    @ManyToOne
    @JoinColumn(name = "id_state", nullable = false,
            foreignKey = @ForeignKey(name = "fk_resolution_state"))
    private StateResolution state;

}
