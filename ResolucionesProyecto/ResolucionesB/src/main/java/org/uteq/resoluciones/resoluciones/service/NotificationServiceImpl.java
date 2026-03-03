package org.uteq.resoluciones.resoluciones.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.resoluciones.resoluciones.dto.NotificationResponse;
import org.uteq.resoluciones.resoluciones.entities.Notification;
import org.uteq.resoluciones.resoluciones.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepo;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> listByUser(Long userId) {
        return notificationRepo.findByUser4IdOrderByDateDesc(userId)
                .stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnread(Long userId) {
        return notificationRepo.countByUser4IdAndStateFalse(userId);
    }

    @Override
    public NotificationResponse markAsRead(Long notificationId) {
        Notification n = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notificacion no encontrada"));
        n.setState(true);
        n.setReadIn(LocalDateTime.now());
        notificationRepo.save(n);
        return toDto(n);
    }

    @Override
    public void markAllAsRead(Long userId) {
        List<Notification> unread = notificationRepo.findByUser4IdOrderByDateDesc(userId)
                .stream().filter(n -> !Boolean.TRUE.equals(n.getState())).toList();
        for (Notification n : unread) {
            n.setState(true);
            n.setReadIn(LocalDateTime.now());
        }
        notificationRepo.saveAll(unread);
    }

    private NotificationResponse toDto(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .message(n.getMessage())
                .date(n.getDate())
                .state(n.getState())
                .readIn(n.getReadIn())
                .channel(n.getChannel())
                .build();
    }
}
