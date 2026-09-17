package com.example.inventoryservice.service;

import com.example.inventoryservice.domain.InventoryItem;
import com.example.inventoryservice.repository.InventoryItemRepository;
import com.example.inventoryservice.web.dto.*;
import com.example.inventoryservice.web.error.ResourceNotFoundException;
import java.util.UUID;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class InventoryItemService {
  private final InventoryItemRepository repository;

  public InventoryItemService(InventoryItemRepository repository) {
    this.repository = repository;
  }

  public Page<InventoryItemResponse> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(this::toResponse);
  }

  public InventoryItemResponse findById(UUID id) {
    return toResponse(get(id));
  }

  @Transactional
  public InventoryItemResponse create(InventoryItemRequest request) {
    InventoryItem entity = new InventoryItem();
    apply(entity, request);
    return toResponse(repository.save(entity));
  }

  @Transactional
  public InventoryItemResponse update(UUID id, InventoryItemRequest request) {
    InventoryItem entity = get(id);
    apply(entity, request);
    return toResponse(repository.save(entity));
  }

  @Transactional
  public void delete(UUID id) {
    repository.delete(get(id));
  }

  private InventoryItem get(UUID id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("InventoryItem not found: " + id));
  }

  private void apply(InventoryItem entity, InventoryItemRequest request) {
    entity.setProductId(request.productId());
    entity.setQuantity(request.quantity());
    entity.setReservedQuantity(request.reservedQuantity());
  }

  private InventoryItemResponse toResponse(InventoryItem entity) {
    return new InventoryItemResponse(
        entity.getId(), entity.getProductId(), entity.getQuantity(), entity.getReservedQuantity());
  }
}
