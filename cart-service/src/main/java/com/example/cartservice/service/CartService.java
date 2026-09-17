package com.example.cartservice.service;

import com.example.cartservice.domain.Cart;
import com.example.cartservice.repository.CartRepository;
import com.example.cartservice.web.dto.*;
import com.example.cartservice.web.error.ResourceNotFoundException;
import java.util.UUID;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CartService {
  private final CartRepository repository;

  public CartService(CartRepository repository) {
    this.repository = repository;
  }

  public Page<CartResponse> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(this::toResponse);
  }

  public CartResponse findById(UUID id) {
    return toResponse(get(id));
  }

  @Transactional
  public CartResponse create(CartRequest request) {
    Cart entity = new Cart();
    apply(entity, request);
    return toResponse(repository.save(entity));
  }

  @Transactional
  public CartResponse update(UUID id, CartRequest request) {
    Cart entity = get(id);
    apply(entity, request);
    return toResponse(repository.save(entity));
  }

  @Transactional
  public void delete(UUID id) {
    repository.delete(get(id));
  }

  private Cart get(UUID id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Cart not found: " + id));
  }

  private void apply(Cart entity, CartRequest request) {
    entity.setUserId(request.userId());
    entity.setStatus(request.status());
  }

  private CartResponse toResponse(Cart entity) {
    return new CartResponse(entity.getId(), entity.getUserId(), entity.getStatus());
  }
}
