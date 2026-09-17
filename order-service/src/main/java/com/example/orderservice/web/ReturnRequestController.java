package com.example.orderservice.web;

import com.example.orderservice.service.ReturnRequestService;
import com.example.orderservice.web.dto.ReturnRequestDtos.InspectReturnRequest;
import com.example.orderservice.web.dto.ReturnRequestDtos.MonthlyRefundTotal;
import com.example.orderservice.web.dto.ReturnRequestDtos.OffsetPage;
import com.example.orderservice.web.dto.ReturnRequestDtos.RaiseReturnRequest;
import com.example.orderservice.web.dto.ReturnRequestDtos.RefundPaymentResponse;
import com.example.orderservice.web.dto.ReturnRequestDtos.ReturnDecision;
import com.example.orderservice.web.dto.ReturnRequestDtos.ReturnRequestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Returns")
public class ReturnRequestController {
  private final ReturnRequestService service;

  public ReturnRequestController(ReturnRequestService service) {
    this.service = service;
  }

  @PostMapping("/return-requests")
  @Operation(summary = "Raise a return request for a delivered order")
  public ResponseEntity<ReturnRequestResponse> raise(
      @Valid @RequestBody RaiseReturnRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.raise(request));
  }

  @GetMapping("/customers/{customerId}/return-requests")
  @Operation(summary = "Track customer return requests")
  public ResponseEntity<OffsetPage<ReturnRequestResponse>> list(
      @PathVariable UUID customerId,
      @RequestParam(defaultValue = "0") long offset,
      @RequestParam(defaultValue = "20") int limit) {
    if (offset < 0 || limit < 1 || limit > 100) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(service.findByCustomer(customerId, offset, limit));
  }

  @PostMapping("/return-requests/{id}/inspect")
  @Operation(summary = "Inspect a return request")
  public ResponseEntity<ReturnRequestResponse> inspect(
      @PathVariable UUID id, @Valid @RequestBody InspectReturnRequest request) {
    return ResponseEntity.ok(service.inspect(id, request));
  }

  @PostMapping("/return-requests/{id}/approve")
  @Operation(summary = "Approve a return request, restore inventory, and initiate a refund")
  public ResponseEntity<ReturnRequestResponse> approve(
      @PathVariable UUID id, @Valid @RequestBody ReturnDecision request) {
    return ResponseEntity.ok(service.approve(id, request));
  }

  @PostMapping("/return-requests/{id}/reject")
  @Operation(summary = "Reject a return request")
  public ResponseEntity<ReturnRequestResponse> reject(
      @PathVariable UUID id, @Valid @RequestBody ReturnDecision request) {
    return ResponseEntity.ok(service.reject(id, request));
  }

  @PostMapping("/refund-payments/{id}/complete")
  @Operation(summary = "Mark an initiated refund as completed")
  public ResponseEntity<RefundPaymentResponse> completeRefund(@PathVariable UUID id) {
    return ResponseEntity.ok(service.completeRefund(id));
  }

  @GetMapping("/admin/reports/refunds/monthly")
  @Operation(summary = "Generate total refund amounts by month")
  public ResponseEntity<List<MonthlyRefundTotal>> monthlyRefundReport() {
    return ResponseEntity.ok(service.monthlyRefundTotals());
  }
}
