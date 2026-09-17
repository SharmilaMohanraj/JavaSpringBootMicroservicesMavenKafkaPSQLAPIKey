package com.example.orderservice.service;

import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderStatus;
import com.example.orderservice.domain.RefundPayment;
import com.example.orderservice.domain.ReturnRequest;
import com.example.orderservice.domain.ReturnStatus;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.RefundPaymentRepository;
import com.example.orderservice.repository.ReturnRequestRepository;
import com.example.orderservice.web.dto.ReturnRequestDtos.InspectReturnRequest;
import com.example.orderservice.web.dto.ReturnRequestDtos.MonthlyRefundTotal;
import com.example.orderservice.web.dto.ReturnRequestDtos.OffsetPage;
import com.example.orderservice.web.dto.ReturnRequestDtos.RaiseReturnRequest;
import com.example.orderservice.web.dto.ReturnRequestDtos.RefundPaymentResponse;
import com.example.orderservice.web.dto.ReturnRequestDtos.ReturnDecision;
import com.example.orderservice.web.dto.ReturnRequestDtos.ReturnRequestResponse;
import com.example.orderservice.web.error.ResourceNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReturnRequestService {
  private final ReturnRequestRepository returnRequests;
  private final RefundPaymentRepository refundPayments;
  private final OrderRepository orders;
  private final RestClient restClient;

  public ReturnRequestService(
      ReturnRequestRepository returnRequests,
      RefundPaymentRepository refundPayments,
      OrderRepository orders,
      @Value("${app.inventory.base-url:http://localhost:22482}") String inventoryBaseUrl) {
    this.returnRequests = returnRequests;
    this.refundPayments = refundPayments;
    this.orders = orders;
    this.restClient = RestClient.builder().baseUrl(inventoryBaseUrl).build();
  }

  @Transactional
  public ReturnRequestResponse raise(RaiseReturnRequest request) {
    Order order =
        orders
            .findById(request.orderId())
            .orElseThrow(
                () -> new ResourceNotFoundException("Order not found: " + request.orderId()));
    if (order.getStatus() != OrderStatus.DELIVERED) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Returns may only be raised for delivered orders");
    }
    ReturnRequest entity = new ReturnRequest();
    entity.setCustomerId(request.customerId());
    entity.setOrderId(request.orderId());
    entity.setInventoryItemId(request.inventoryItemId());
    entity.setQuantity(request.quantity());
    entity.setReason(request.reason());
    entity.setStatus(ReturnStatus.RETURN_REQUESTED);
    entity.setCreatedAt(Instant.now());
    return toResponse(returnRequests.save(entity));
  }

  public OffsetPage<ReturnRequestResponse> findByCustomer(UUID customerId, long offset, int limit) {
    Pageable pageable = PageRequest.of(Math.toIntExact(offset / limit), limit);
    var page = returnRequests.findByCustomerId(customerId, pageable);
    return new OffsetPage<>(
        page.map(this::toResponse).getContent(), page.getTotalElements(), limit, offset);
  }

  public ReturnRequestResponse inspect(UUID id, InspectReturnRequest request) {
    ReturnRequest entity = getRequest(id);
    requireRequested(entity);
    entity.setWarehouseStaffId(request.warehouseStaffId());
    entity.setInspectionNotes(request.notes());
    return toResponse(returnRequests.save(entity));
  }

  @Transactional
  public ReturnRequestResponse approve(UUID id, ReturnDecision request) {
    ReturnRequest entity = getRequest(id);
    requireRequested(entity);
    entity.setWarehouseStaffId(request.warehouseStaffId());
    entity.setInspectionNotes(request.notes());
    entity.setStatus(ReturnStatus.RETURN_APPROVED);
    ReturnRequest approved = returnRequests.save(entity);
    restoreInventory(approved);
    RefundPayment payment = new RefundPayment();
    payment.setReturnRequestId(approved.getId());
    payment.setAmount(orderFor(approved).getTotal());
    payment.setStatus(ReturnStatus.REFUND_INITIATED);
    payment.setCreatedAt(Instant.now());
    refundPayments.save(payment);
    approved.setStatus(ReturnStatus.REFUND_INITIATED);
    return toResponse(returnRequests.save(approved));
  }

  public ReturnRequestResponse reject(UUID id, ReturnDecision request) {
    ReturnRequest entity = getRequest(id);
    requireRequested(entity);
    entity.setWarehouseStaffId(request.warehouseStaffId());
    entity.setInspectionNotes(request.notes());
    entity.setStatus(ReturnStatus.RETURN_REJECTED);
    return toResponse(returnRequests.save(entity));
  }

  @Transactional
  public RefundPaymentResponse completeRefund(UUID id) {
    RefundPayment payment =
        refundPayments
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Refund payment not found: " + id));
    if (payment.getStatus() != ReturnStatus.REFUND_INITIATED) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Refund is not awaiting completion");
    }
    payment.setStatus(ReturnStatus.REFUND_COMPLETED);
    RefundPayment saved = refundPayments.save(payment);
    ReturnRequest request = getRequest(saved.getReturnRequestId());
    request.setStatus(ReturnStatus.REFUND_COMPLETED);
    returnRequests.save(request);
    return toResponse(saved);
  }

  public List<MonthlyRefundTotal> monthlyRefundTotals() {
    return refundPayments.findAll().stream()
        .collect(
            java.util.stream.Collectors.groupingBy(
                payment -> YearMonth.from(payment.getCreatedAt().atZone(ZoneOffset.UTC)),
                java.util.TreeMap::new,
                java.util.stream.Collectors.reducing(
                    BigDecimal.ZERO, RefundPayment::getAmount, BigDecimal::add)))
        .entrySet()
        .stream()
        .map(entry -> new MonthlyRefundTotal(entry.getKey(), entry.getValue()))
        .toList();
  }

  private void restoreInventory(ReturnRequest request) {
    restClient
        .post()
        .uri("/api/v1/inventory-items/{id}/restore", request.getInventoryItemId())
        .body(new RestoreInventoryRequest(request.getQuantity()))
        .retrieve()
        .toBodilessEntity();
  }

  private Order orderFor(ReturnRequest request) {
    return orders
        .findById(request.getOrderId())
        .orElseThrow(
            () -> new ResourceNotFoundException("Order not found: " + request.getOrderId()));
  }

  private ReturnRequest getRequest(UUID id) {
    return returnRequests
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Return request not found: " + id));
  }

  private void requireRequested(ReturnRequest request) {
    if (request.getStatus() != ReturnStatus.RETURN_REQUESTED) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Return request has already been decided");
    }
  }

  private ReturnRequestResponse toResponse(ReturnRequest request) {
    return new ReturnRequestResponse(
        request.getId(),
        request.getCustomerId(),
        request.getOrderId(),
        request.getInventoryItemId(),
        request.getQuantity(),
        request.getReason(),
        request.getWarehouseStaffId(),
        request.getInspectionNotes(),
        request.getStatus(),
        request.getCreatedAt());
  }

  private RefundPaymentResponse toResponse(RefundPayment payment) {
    return new RefundPaymentResponse(
        payment.getId(),
        payment.getReturnRequestId(),
        payment.getAmount(),
        payment.getStatus(),
        payment.getCreatedAt());
  }

  private record RestoreInventoryRequest(int quantity) {}
}
