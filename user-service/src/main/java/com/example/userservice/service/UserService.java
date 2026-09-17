package com.example.userservice.service;

import com.example.userservice.domain.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.web.dto.*;
import com.example.userservice.web.error.ResourceNotFoundException;
import java.util.UUID;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {
  private final UserRepository repository;

  public UserService(UserRepository repository) {
    this.repository = repository;
  }

  public Page<UserResponse> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(this::toResponse);
  }

  public UserResponse findById(UUID id) {
    return toResponse(get(id));
  }

  @Transactional
  public UserResponse create(UserRequest request) {
    User entity = new User();
    apply(entity, request);
    return toResponse(repository.save(entity));
  }

  @Transactional
  public UserResponse update(UUID id, UserRequest request) {
    User entity = get(id);
    apply(entity, request);
    return toResponse(repository.save(entity));
  }

  @Transactional
  public void delete(UUID id) {
    repository.delete(get(id));
  }

  private User get(UUID id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
  }

  private void apply(User entity, UserRequest request) {
    entity.setEmail(request.email());
    entity.setDisplayName(request.displayName());
    entity.setActive(request.active());
  }

  private UserResponse toResponse(User entity) {
    return new UserResponse(
        entity.getId(), entity.getEmail(), entity.getDisplayName(), entity.getActive());
  }
}
