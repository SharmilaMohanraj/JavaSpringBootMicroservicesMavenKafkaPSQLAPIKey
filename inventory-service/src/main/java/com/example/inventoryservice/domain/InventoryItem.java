package com.example.inventoryservice.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "inventory_items")
public class InventoryItem {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private java.util.UUID productId;
  private int quantity;
  private int reservedQuantity;

  public UUID getId() {
    return id;
  }

  public java.util.UUID getProductId() {
    return productId;
  }

  public void setProductId(java.util.UUID productId) {
    this.productId = productId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public int getReservedQuantity() {
    return reservedQuantity;
  }

  public void setReservedQuantity(int reservedQuantity) {
    this.reservedQuantity = reservedQuantity;
  }
}
