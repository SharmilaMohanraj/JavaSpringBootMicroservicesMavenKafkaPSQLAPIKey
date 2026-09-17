package com.example.orderservice.web.dto;

import com.example.orderservice.domain.ReturnStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

public final class ReturnRequestDtos {
  private ReturnRequestDtos() {
  }

  public record RaiseReturnRequest(
      @NotNull UUID customerId,
      @NotNull UUID orderId,
      @NotNull UUID inventoryItemId,
      @Min(1) int quantity,
      @NotBlank String reason) {
  }

  public record InspectReturnRequest(@NotNull UUID warehouseStaffId, @NotBlank String notes) {
  }

  public record ReturnDecision(@NotNull UUID warehouseStaffId, @NotBlank String notes) {
  }

  public record ReturnRequestResponse(
      UUID id,
      UUID customerId,
      UUID orderId,
      UUID inventoryItemId,
      int quantity,
      String reason,
      UUID warehouseStaffId,
      String inspectionNotes,
      ReturnStatus status,
      Instant createdAt) {
  }

  public record RefundPaymentResponse(
      UUID id, UUID returnRequestId, BigDecimal amount, ReturnStatus status, Instant createdAt) {
  }

  public record MonthlyRefundTotal(YearMonth month, BigDecimal totalRefundAmount) {
  }

  public record OffsetPage<T>(List<T> content, long total, int limit, long offset) {
  }
}
