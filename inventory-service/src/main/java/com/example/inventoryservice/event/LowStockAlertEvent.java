package com.example.inventoryservice.event;

import java.time.Instant;
import java.util.UUID;

public record LowStockAlertEvent(
    UUID inventoryItemId,
    UUID productId,
    int quantity,
    int lowStockThreshold,
    String eventType,
    Instant occurredAt) {}
