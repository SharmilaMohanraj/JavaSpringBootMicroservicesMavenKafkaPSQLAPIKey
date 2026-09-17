package com.example.orderservice.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private UUID userId;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal total;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderStatus status;

  public UUID getId() {
    return id;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID v) {
    userId = v;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal v) {
    total = v;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus v) {
    status = v;
  }
}
