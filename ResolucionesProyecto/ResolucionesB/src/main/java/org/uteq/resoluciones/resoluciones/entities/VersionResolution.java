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
@Table(name = "tb_version_resolution")
public class VersionResolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_version")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_resolution", nullable = false,
            foreignKey = @ForeignKey(name = "fk_version_resolution"))
    private Resolution resolution3;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Lob
    @Column(name = "content", nullable = false, columnDefinition = "text")
    private String content;

    @Column(name = "date")
    private LocalDateTime date;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_user", foreignKey = @ForeignKey(name = "fk_version_user"))
    private User user6;

    @Column(name = "reason", length = 300)
    private String reason;
}
