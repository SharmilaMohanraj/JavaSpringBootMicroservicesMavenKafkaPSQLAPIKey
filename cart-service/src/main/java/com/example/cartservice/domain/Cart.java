package com.example.cartservice.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "carts")
public class Cart {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private java.util.UUID userId;
  private String status;

  public UUID getId() {
    return id;
  }

  public java.util.UUID getUserId() {
    return userId;
  }

  public void setUserId(java.util.UUID userId) {
    this.userId = userId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
