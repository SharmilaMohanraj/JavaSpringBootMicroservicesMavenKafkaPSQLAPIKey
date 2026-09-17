package com.example.orderservice.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "coupon_applications")
public class CouponApplication {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private UUID orderId;

  @Column(nullable = false)
  private String couponCode;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal discountAmount;

  public UUID getId() {
    return id;
  }

  public UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(UUID v) {
    orderId = v;
  }

  public String getCouponCode() {
    return couponCode;
  }

  public void setCouponCode(String v) {
    couponCode = v;
  }

  public BigDecimal getDiscountAmount() {
    return discountAmount;
  }

  public void setDiscountAmount(BigDecimal v) {
    discountAmount = v;
  }
}
