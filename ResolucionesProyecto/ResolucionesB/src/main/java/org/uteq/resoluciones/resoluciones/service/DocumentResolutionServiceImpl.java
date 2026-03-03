package org.uteq.resoluciones.resoluciones.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.uteq.resoluciones.resoluciones.dto.DocumentResolutionResponse;
import org.uteq.resoluciones.resoluciones.entities.*;
import org.uteq.resoluciones.resoluciones.repository.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentResolutionServiceImpl implements DocumentResolutionService {

    private final DocumentResolutionRepository docRepo;
    private final ResolutionRepository resolutionRepo;
    private final UserRepository userRepo;
    private final BinnacleRepository binnacleRepo;

    @Value("${app.upload.dir:uploads/}")
    private String uploadDir;

    @Override
    @Transactional(readOnly = true)
    public List<DocumentResolutionResponse> listByResolution(Long resolutionId) {
        return docRepo.findByResolution1IdOrderByUploadDateDesc(resolutionId)
                .stream().map(this::toDto).toList();
    }

    @Override
    public DocumentResolutionResponse upload(Long resolutionId, MultipartFile file, Long userId) {
        Resolution resolution = resolutionRepo.findById(resolutionId)
                .orElseThrow(() -> new EntityNotFoundException("Resolucion no encontrada"));
        User user = userId != null ? userRepo.findById(userId).orElse(null) : null;

        try {
            Path dir = Paths.get(uploadDir, "resolutions", String.valueOf(resolutionId));
            Files.createDirectories(dir);

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = dir.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String hash = computeSha256(file.getBytes());

            DocumentResolution doc = new DocumentResolution();
            doc.setResolution1(resolution);
            doc.setName(file.getOriginalFilename());
            doc.setRoute(filePath.toString());
            doc.setTipMime(file.getContentType());
            doc.setSizeBytes(file.getSize());
            doc.setHashSha256(hash);
            doc.setUploadDate(LocalDateTime.now());
            doc = docRepo.save(doc);

            if (user != null) {
                Binnacle b = new Binnacle();
                b.setUser2(user);
                b.setResolution3(resolution);
                b.setAction("Subio documento: " + file.getOriginalFilename());
                b.setDate(LocalDateTime.now());
                binnacleRepo.save(b);
            }

            return toDto(doc);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar archivo", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Resource download(Long documentId) {
        DocumentResolution doc = docRepo.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Documento no encontrado"));
        try {
            Path path = Paths.get(doc.getRoute());
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists()) throw new EntityNotFoundException("Archivo no encontrado en disco");
            return resource;
        } catch (Exception e) {
            throw new RuntimeException("Error al leer archivo", e);
        }
    }

    @Override
    public void delete(Long documentId, Long userId) {
        DocumentResolution doc = docRepo.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Documento no encontrado"));
        try {
            Files.deleteIfExists(Paths.get(doc.getRoute()));
        } catch (IOException ignored) {}

        if (userId != null) {
            User user = userRepo.findById(userId).orElse(null);
            if (user != null) {
                Binnacle b = new Binnacle();
                b.setUser2(user);
                b.setResolution3(doc.getResolution1());
                b.setAction("Elimino documento: " + doc.getName());
                b.setDate(LocalDateTime.now());
                binnacleRepo.save(b);
            }
        }
        docRepo.delete(doc);
    }

    private String computeSha256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            return "N/A";
        }
    }

    private DocumentResolutionResponse toDto(DocumentResolution d) {
        return DocumentResolutionResponse.builder()
                .id(d.getId())
                .resolutionId(d.getResolution1().getId())
                .name(d.getName())
                .tipMime(d.getTipMime())
                .sizeBytes(d.getSizeBytes())
                .hashSha256(d.getHashSha256())
                .uploadDate(d.getUploadDate())
                .build();
    }
}
