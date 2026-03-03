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
@Table(name="tb_delegation")
public class Delegation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_delegation")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_origin_user", nullable = false,
            foreignKey = @ForeignKey(name = "fk_delegation_origin_user"))
    private User originUser;

    @ManyToOne
    @JoinColumn(name = "id_destinity_user", nullable = false,
            foreignKey = @ForeignKey(name = "fk_delegation_destinity_user"))
    private User destinityUser;

    @Column(name = "reason", length = 200)
    private String reason;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;
}
