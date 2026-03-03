package org.uteq.resoluciones.resoluciones.service;

import org.uteq.resoluciones.resoluciones.dto.NotificationResponse;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> listByUser(Long userId);
    long countUnread(Long userId);
    NotificationResponse markAsRead(Long notificationId);
    void markAllAsRead(Long userId);
}
