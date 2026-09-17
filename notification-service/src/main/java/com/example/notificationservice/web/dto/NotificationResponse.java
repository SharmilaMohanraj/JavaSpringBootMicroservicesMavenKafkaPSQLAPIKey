package com.example.notificationservice.web.dto;

import com.example.notificationservice.domain.NotificationStatus;
import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
    UUID id,
    UUID userId,
    String eventType,
    String message,
    NotificationStatus readStatus,
    Instant createdAt) {}
