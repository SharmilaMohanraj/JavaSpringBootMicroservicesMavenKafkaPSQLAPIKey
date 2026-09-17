package com.example.orderservice.service;

import com.example.orderservice.domain.CouponApplication;
import com.example.orderservice.repository.CouponApplicationRepository;
import com.example.orderservice.web.dto.*;
import com.example.orderservice.web.error.ResourceNotFoundException;
import java.util.UUID;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouponApplicationService {
  private final CouponApplicationRepository repository;

  public CouponApplicationService(CouponApplicationRepository repository) {
    this.repository = repository;
  }

  public Page<CouponApplicationResponse> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(this::toResponse);
  }

  public CouponApplicationResponse findById(UUID id) {
    return toResponse(get(id));
  }

  @Transactional
  public CouponApplicationResponse create(CouponApplicationRequest request) {
    CouponApplication entity = new CouponApplication();
    apply(entity, request);
    return toResponse(repository.save(entity));
  }

  @Transactional
  public CouponApplicationResponse update(UUID id, CouponApplicationRequest request) {
    CouponApplication entity = get(id);
    apply(entity, request);
    return toResponse(repository.save(entity));
  }

  @Transactional
  public void delete(UUID id) {
    repository.delete(get(id));
  }

  private CouponApplication get(UUID id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("CouponApplication not found: " + id));
  }

  private void apply(CouponApplication entity, CouponApplicationRequest request) {
    entity.setOrderId(request.orderId());
    entity.setCouponCode(request.couponCode());
    entity.setDiscountAmount(request.discountAmount());
  }

  private CouponApplicationResponse toResponse(CouponApplication entity) {
    return new CouponApplicationResponse(
        entity.getId(), entity.getOrderId(), entity.getCouponCode(), entity.getDiscountAmount());
  }
}
