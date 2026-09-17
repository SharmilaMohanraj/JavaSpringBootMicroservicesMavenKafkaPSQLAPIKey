package com.example.notificationservice.messaging;

import java.time.Instant;
import java.util.UUID;

public record OrderStatusChangedEvent(
    UUID orderId,
    UUID userId,
    String previousStatus,
    String status,
    String eventType,
    Instant occurredAt) {}
