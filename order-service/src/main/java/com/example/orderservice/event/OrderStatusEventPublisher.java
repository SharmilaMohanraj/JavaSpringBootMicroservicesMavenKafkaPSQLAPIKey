package com.example.orderservice.event;

import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderStatus;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusEventPublisher {
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final String topic;

  public OrderStatusEventPublisher(
      KafkaTemplate<String, Object> kafkaTemplate,
      @Value("${app.kafka.topics.order-status}") String topic) {
    this.kafkaTemplate = kafkaTemplate;
    this.topic = topic;
  }

  public void publish(Order order, OrderStatus previousStatus) {
    OrderStatusChangedEvent event =
        new OrderStatusChangedEvent(
            order.getId(),
            order.getUserId(),
            previousStatus.name(),
            order.getStatus().name(),
            "ORDER_" + order.getStatus().name(),
            Instant.now());
    kafkaTemplate.send(topic, order.getId().toString(), event);
  }
}
