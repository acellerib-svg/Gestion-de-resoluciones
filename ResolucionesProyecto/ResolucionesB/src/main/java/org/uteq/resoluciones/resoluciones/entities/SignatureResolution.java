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
@Table(name = "tb_signature_resolution")
public class SignatureResolution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_signature")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_resolution", nullable = false,
            foreignKey = @ForeignKey(name = "fk_signature_resolution"))
    private Resolution resolution2;

    @ManyToOne
    @JoinColumn(name = "id_user_login", nullable = false,
            foreignKey = @ForeignKey(name = "fk_signature_user"))
    private User user2;

    @Column(name = "position", nullable = false, length = 100)
    private String position;

    @Column(name = "signature_date")
    private LocalDateTime signatureDate;

    @Column(name = "signature_route", length = 300)
    private String signatureRoute;

    @Column(name = "state", length = 30)
    private String state;

    @Column(name = "observations", length = 500)
    private String observations;
}
