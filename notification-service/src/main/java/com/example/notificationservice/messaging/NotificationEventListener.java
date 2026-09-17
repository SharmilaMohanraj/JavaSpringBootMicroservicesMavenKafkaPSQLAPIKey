package com.example.notificationservice.messaging;

import com.example.notificationservice.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {
  private final ObjectMapper objectMapper;
  private final NotificationService notificationService;

  public NotificationEventListener(ObjectMapper objectMapper, NotificationService notificationService) {
    this.objectMapper = objectMapper;
    this.notificationService = notificationService;
  }

  @KafkaListener(topics = "${app.kafka.topics.order-status}")
  public void onOrderStatusChanged(String payload) {
    OrderStatusChangedEvent event = read(payload, OrderStatusChangedEvent.class);
    notificationService.create(
        event.userId(),
        event.eventType(),
        "Order " + event.orderId() + " status changed from " + event.previousStatus() + " to " + event.status() + ".");
  }

  @KafkaListener(topics = "${app.kafka.topics.low-stock}")
  public void onLowStockAlert(String payload) {
    LowStockAlertEvent event = read(payload, LowStockAlertEvent.class);
    notificationService.create(
        null,
        event.eventType(),
        "Low stock alert for product " + event.productId() + ": " + event.quantity()
            + " remaining (threshold: " + event.lowStockThreshold() + ").");
  }

  private <T> T read(String payload, Class<T> eventType) {
    try {
      return objectMapper.readValue(payload, eventType);
    } catch (JsonProcessingException exception) {
      throw new IllegalArgumentException("Unable to deserialize notification event", exception);
    }
  }
}
