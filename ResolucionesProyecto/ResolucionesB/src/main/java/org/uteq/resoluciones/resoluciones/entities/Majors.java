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
@Table(name = "tb_major")
public class Majors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_major")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_faculty", nullable = false, foreignKey = @ForeignKey(name = "fk_majors_facultys"))
    private Faculty faculty;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name="description", nullable = false, length = 300)
    private String description;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
