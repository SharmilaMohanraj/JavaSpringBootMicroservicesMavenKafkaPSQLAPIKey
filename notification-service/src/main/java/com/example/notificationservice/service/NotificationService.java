package com.example.notificationservice.service;

import com.example.notificationservice.domain.Notification;
import com.example.notificationservice.domain.NotificationStatus;
import com.example.notificationservice.repository.NotificationRepository;
import com.example.notificationservice.web.dto.NotificationResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class NotificationService {
  private final NotificationRepository repository;

  public NotificationService(NotificationRepository repository) {
    this.repository = repository;
  }

  public List<NotificationResponse> findUnread() {
    return repository.findByReadStatusOrderByCreatedAtDesc(NotificationStatus.UNREAD).stream()
        .map(this::toResponse)
        .toList();
  }

  @Transactional
  public void create(UUID userId, String eventType, String message) {
    Notification notification = new Notification();
    notification.setUserId(userId);
    notification.setEventType(eventType);
    notification.setMessage(message);
    notification.setReadStatus(NotificationStatus.UNREAD);
    notification.setCreatedAt(Instant.now());
    repository.save(notification);
  }

  @Transactional
  public NotificationResponse markRead(UUID id) {
    Notification notification =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Notification not found: " + id));
    notification.setReadStatus(NotificationStatus.READ);
    return toResponse(repository.save(notification));
  }

  private NotificationResponse toResponse(Notification notification) {
    return new NotificationResponse(
        notification.getId(),
        notification.getUserId(),
        notification.getEventType(),
        notification.getMessage(),
        notification.getReadStatus(),
        notification.getCreatedAt());
  }
}
