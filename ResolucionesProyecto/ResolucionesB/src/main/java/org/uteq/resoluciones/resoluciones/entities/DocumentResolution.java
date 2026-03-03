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
@Table(name = "tb_document_resolution")
public class DocumentResolution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_document")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_resolution", nullable = false,
            foreignKey = @ForeignKey(name = "fk_document_resolution"))
    private Resolution resolution1;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "route", length = 300)
    private String route;

    @Column(name = "tip_mime", length = 100)
    private String tipMime;

    @Column(name = "size_bytes")
    private Long sizeBytes;

    @Column(name = "hash_sha256", length = 64)
        private String hashSha256;

    @Column(name = "upload_date", nullable = false)
    private LocalDateTime uploadDate;
}
