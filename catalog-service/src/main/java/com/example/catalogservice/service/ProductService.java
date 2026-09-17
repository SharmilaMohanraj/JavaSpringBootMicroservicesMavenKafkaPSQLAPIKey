package com.example.catalogservice.service;

import com.example.catalogservice.domain.Product;
import com.example.catalogservice.repository.ProductRepository;
import com.example.catalogservice.web.dto.*;
import com.example.catalogservice.web.error.ResourceNotFoundException;
import java.util.UUID;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {
  private final ProductRepository repository;

  public ProductService(ProductRepository repository) {
    this.repository = repository;
  }

  public Page<ProductResponse> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(this::toResponse);
  }

  public ProductResponse findById(UUID id) {
    return toResponse(get(id));
  }

  @Transactional
  public ProductResponse create(ProductRequest request) {
    Product entity = new Product();
    apply(entity, request);
    return toResponse(repository.save(entity));
  }

  @Transactional
  public ProductResponse update(UUID id, ProductRequest request) {
    Product entity = get(id);
    apply(entity, request);
    return toResponse(repository.save(entity));
  }

  @Transactional
  public void delete(UUID id) {
    repository.delete(get(id));
  }

  private Product get(UUID id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
  }

  private void apply(Product entity, ProductRequest request) {
    entity.setSku(request.sku());
    entity.setName(request.name());
    entity.setDescription(request.description());
    entity.setPrice(request.price());
    entity.setCategory(request.category());
    entity.setActive(request.active());
  }

  private ProductResponse toResponse(Product entity) {
    return new ProductResponse(
        entity.getId(),
        entity.getSku(),
        entity.getName(),
        entity.getDescription(),
        entity.getPrice(),
        entity.getCategory(),
        entity.getActive());
  }
}
