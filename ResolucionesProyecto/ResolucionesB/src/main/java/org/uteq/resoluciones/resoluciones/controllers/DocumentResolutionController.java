package org.uteq.resoluciones.resoluciones.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.uteq.resoluciones.resoluciones.dto.DocumentResolutionResponse;
import org.uteq.resoluciones.resoluciones.service.DocumentResolutionService;

import java.util.List;

@RestController
@RequestMapping("/api/resolutions/{resolutionId}/documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DocumentResolutionController {

    private final DocumentResolutionService service;

    @GetMapping
    public List<DocumentResolutionResponse> list(@PathVariable Long resolutionId) {
        return service.listByResolution(resolutionId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentResolutionResponse upload(
            @PathVariable Long resolutionId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) Long userId) {
        return service.upload(resolutionId, file, userId);
    }

    @GetMapping("/{docId}/download")
    public ResponseEntity<Resource> download(@PathVariable Long resolutionId, @PathVariable Long docId) {
        Resource resource = service.download(docId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{docId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long resolutionId, @PathVariable Long docId,
                       @RequestParam(required = false) Long userId) {
        service.delete(docId, userId);
    }
}
