package com.example.orderservice.service;

import com.example.orderservice.domain.Order;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.web.dto.*;
import com.example.orderservice.web.error.ResourceNotFoundException;
import java.util.UUID;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {
  private final OrderRepository repository;

  public OrderService(OrderRepository repository) {
    this.repository = repository;
  }

  public Page<OrderResponse> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(this::toResponse);
  }

  public OrderResponse findById(UUID id) {
    return toResponse(get(id));
  }

  @Transactional
  public OrderResponse create(OrderRequest request) {
    Order entity = new Order();
    apply(entity, request);
    return toResponse(repository.save(entity));
  }

  @Transactional
  public OrderResponse update(UUID id, OrderRequest request) {
    Order entity = get(id);
    apply(entity, request);
    return toResponse(repository.save(entity));
  }

  @Transactional
  public void delete(UUID id) {
    repository.delete(get(id));
  }

  private Order get(UUID id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
  }

  private void apply(Order entity, OrderRequest request) {
    entity.setUserId(request.userId());
    entity.setTotal(request.total());
    entity.setStatus(request.status());
  }

  private OrderResponse toResponse(Order entity) {
    return new OrderResponse(
        entity.getId(), entity.getUserId(), entity.getTotal(), entity.getStatus());
  }
}
