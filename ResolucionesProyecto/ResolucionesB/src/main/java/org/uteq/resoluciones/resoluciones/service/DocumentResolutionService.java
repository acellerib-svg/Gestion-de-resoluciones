package org.uteq.resoluciones.resoluciones.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import org.uteq.resoluciones.resoluciones.dto.DocumentResolutionResponse;

import java.util.List;

public interface DocumentResolutionService {
    List<DocumentResolutionResponse> listByResolution(Long resolutionId);
    DocumentResolutionResponse upload(Long resolutionId, MultipartFile file, Long userId);
    Resource download(Long documentId);
    void delete(Long documentId, Long userId);
}
