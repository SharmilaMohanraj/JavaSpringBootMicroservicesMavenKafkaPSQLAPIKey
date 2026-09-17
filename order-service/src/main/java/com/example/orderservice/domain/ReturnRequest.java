package com.example.orderservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "return_requests")
public class ReturnRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private UUID customerId;

  @Column(nullable = false)
  private UUID orderId;

  @Column(nullable = false)
  private UUID inventoryItemId;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false)
  private String reason;

  private UUID warehouseStaffId;
  private String inspectionNotes;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReturnStatus status;

  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  public UUID getId() {
    return id;
  }

  public UUID getCustomerId() {
    return customerId;
  }

  public void setCustomerId(UUID customerId) {
    this.customerId = customerId;
  }

  public UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public UUID getInventoryItemId() {
    return inventoryItemId;
  }

  public void setInventoryItemId(UUID inventoryItemId) {
    this.inventoryItemId = inventoryItemId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public UUID getWarehouseStaffId() {
    return warehouseStaffId;
  }

  public void setWarehouseStaffId(UUID warehouseStaffId) {
    this.warehouseStaffId = warehouseStaffId;
  }

  public String getInspectionNotes() {
    return inspectionNotes;
  }

  public void setInspectionNotes(String inspectionNotes) {
    this.inspectionNotes = inspectionNotes;
  }

  public ReturnStatus getStatus() {
    return status;
  }

  public void setStatus(ReturnStatus status) {
    this.status = status;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
