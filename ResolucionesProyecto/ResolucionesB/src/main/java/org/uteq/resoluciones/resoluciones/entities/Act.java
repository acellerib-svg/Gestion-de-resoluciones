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
@Table(name = "tb_act")
public class Act {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_act")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_session", nullable = false, foreignKey = @ForeignKey(name = "fk_acta_sesion1"))
    private Session session1;

    @Column(name = "number_act", nullable = false, length = 30)
    private String numberAct;

    @Column(name = "closed")
    private Boolean closed;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "closing_date")
    private LocalDateTime closingDate;

    @Column(name = "observations", length = 500)
    private String observations;

    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
