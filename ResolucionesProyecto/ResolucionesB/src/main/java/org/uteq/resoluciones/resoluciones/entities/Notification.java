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
@Table(name = "tb_notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user_login", nullable = false,
            foreignKey = @ForeignKey(name = "fk_notification_user4"))
    private User user4;

    @Column(name = "message", length = 300)
    private String message;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "state")
    private Boolean state;

    @Column(name = "read_in")
    private LocalDateTime readIn;

    @Column(name = "channel", length = 30)
    private String channel;
}
