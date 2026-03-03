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
@Table(name = "tb_binnacle")
public class Binnacle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_binnacle")
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name = "id_user_login", nullable = false,
            foreignKey = @ForeignKey(name = "fk_binnacle_user_login"))
    private User user2;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_resolution",
            foreignKey = @ForeignKey(name = "fk_binnacle_resolution"))
    private Resolution resolution3;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_tipe_action",
            foreignKey = @ForeignKey(name = "fk_binnacle_tipe_action"))
    private TipAction tipAction1;

    @Column(name = "action", length = 200)
    private String action;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "ip", length = 50)
    private String ip;

    @Column(name = "user_agent", length = 300)
    private String userAgent;
}
