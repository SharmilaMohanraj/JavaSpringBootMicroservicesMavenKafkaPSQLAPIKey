package com.example.orderservice.service;

import com.example.orderservice.domain.PaymentRecord;
import com.example.orderservice.repository.PaymentRecordRepository;
import com.example.orderservice.web.dto.*;
import com.example.orderservice.web.error.ResourceNotFoundException;
import java.util.UUID;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PaymentRecordService {
  private final PaymentRecordRepository repository;

  public PaymentRecordService(PaymentRecordRepository repository) {
    this.repository = repository;
  }

  public Page<PaymentRecordResponse> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(this::toResponse);
  }

  public PaymentRecordResponse findById(UUID id) {
    return toResponse(get(id));
  }

  @Transactional
  public PaymentRecordResponse create(PaymentRecordRequest request) {
    PaymentRecord entity = new PaymentRecord();
    apply(entity, request);
    return toResponse(repository.save(entity));
  }

  @Transactional
  public PaymentRecordResponse update(UUID id, PaymentRecordRequest request) {
    PaymentRecord entity = get(id);
    apply(entity, request);
    return toResponse(repository.save(entity));
  }

  @Transactional
  public void delete(UUID id) {
    repository.delete(get(id));
  }

  private PaymentRecord get(UUID id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("PaymentRecord not found: " + id));
  }

  private void apply(PaymentRecord entity, PaymentRecordRequest request) {
    entity.setOrderId(request.orderId());
    entity.setAmount(request.amount());
    entity.setStatus(request.status());
  }

  private PaymentRecordResponse toResponse(PaymentRecord entity) {
    return new PaymentRecordResponse(
        entity.getId(), entity.getOrderId(), entity.getAmount(), entity.getStatus());
  }
}
