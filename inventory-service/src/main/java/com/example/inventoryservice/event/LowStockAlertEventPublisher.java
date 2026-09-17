package com.example.inventoryservice.event;

import com.example.inventoryservice.domain.InventoryItem;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class LowStockAlertEventPublisher {
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final String topic;
  private final int lowStockThreshold;

  public LowStockAlertEventPublisher(
      KafkaTemplate<String, Object> kafkaTemplate,
      @Value("${app.kafka.topics.low-stock}") String topic,
      @Value("${app.inventory.low-stock-threshold}") int lowStockThreshold) {
    this.kafkaTemplate = kafkaTemplate;
    this.topic = topic;
    this.lowStockThreshold = lowStockThreshold;
  }

  public void publish(InventoryItem item) {
    LowStockAlertEvent event =
        new LowStockAlertEvent(
            item.getId(),
            item.getProductId(),
            item.getQuantity(),
            lowStockThreshold,
            "LOW_STOCK",
            Instant.now());
    kafkaTemplate.send(topic, item.getProductId().toString(), event);
  }
}
