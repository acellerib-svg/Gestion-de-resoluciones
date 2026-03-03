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
@Table(name="tb_session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_session")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_instance", nullable = false, foreignKey = @ForeignKey(name = "fk_sesion_instance1"))
    private Instance instance1;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "session_number", length = 30)
    private String sessionNumber;

    @Column(name = "tip", length = 50)
    private String tip;

    @Column(name = "closed", nullable = false)
    private Boolean closed;

    @Column(name = "observations", length = 500)
    private String observations;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;


}
