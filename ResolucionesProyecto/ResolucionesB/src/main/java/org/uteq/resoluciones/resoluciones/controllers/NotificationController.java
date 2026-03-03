package org.uteq.resoluciones.resoluciones.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.uteq.resoluciones.resoluciones.dto.NotificationResponse;
import org.uteq.resoluciones.resoluciones.service.NotificationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    public List<NotificationResponse> list(@RequestParam Long userId) {
        return service.listByUser(userId);
    }

    @GetMapping("/count")
    public Map<String, Long> countUnread(@RequestParam Long userId) {
        return Map.of("count", service.countUnread(userId));
    }

    @PatchMapping("/{id}/read")
    public NotificationResponse markAsRead(@PathVariable Long id) {
        return service.markAsRead(id);
    }

    @PatchMapping("/read-all")
    public void markAllAsRead(@RequestParam Long userId) {
        service.markAllAsRead(userId);
    }
}
