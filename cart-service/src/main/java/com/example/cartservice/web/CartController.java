package com.example.cartservice.web;

import com.example.cartservice.service.CartService;
import com.example.cartservice.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@Tag(name = "Carts")
public class CartController {
  private final CartService service;

  public CartController(CartService service) {
    this.service = service;
  }

  @GetMapping
  @Operation(summary = "List carts")
  public Page<CartResponse> list(@PageableDefault(size = 20) Pageable pageable) {
    return service.findAll(pageable);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a Cart")
  public CartResponse get(@PathVariable UUID id) {
    return service.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create a Cart")
  public CartResponse create(@Valid @RequestBody CartRequest request) {
    return service.create(request);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update a Cart")
  public CartResponse update(@PathVariable UUID id, @Valid @RequestBody CartRequest request) {
    return service.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete a Cart")
  public void delete(@PathVariable UUID id) {
    service.delete(id);
  }
}
