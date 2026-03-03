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
@Table(name = "tb_user_session")
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user_session")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user_login", nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_sesion_user"))
    private User user5;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "ip", length = 50)
    private String ip;

    @Column(name = "user_agent", length = 300)
    private String userAgent;

    @Column(name = "forced_closed", nullable = false)
    private Boolean forcedClosed;
}
