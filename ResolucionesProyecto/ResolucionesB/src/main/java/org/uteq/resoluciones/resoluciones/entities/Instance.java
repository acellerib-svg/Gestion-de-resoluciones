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
@Table(name = "tb_instance")
public class Instance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_instance")
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @ManyToOne
    @JoinColumn(name="id_level", nullable = false, foreignKey = @ForeignKey(name="fk_instance_level"))
    private Hierarchicallevel level;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_major", nullable = true, foreignKey = @ForeignKey(name = "fk_instance_major"))
    private Majors major;

    @ManyToOne(optional = true)
    @JoinColumn(name="id_instance_father", foreignKey = @ForeignKey(name = "fk_instance_father"))
    private Instance instanceFather;

    @Column(name="description", length = 300)
    private String description;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;
}
