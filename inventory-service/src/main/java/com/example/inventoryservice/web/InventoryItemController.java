package com.example.inventoryservice.web;

import com.example.inventoryservice.service.InventoryItemService;
import com.example.inventoryservice.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory-items")
@Tag(name = "InventoryItems")
public class InventoryItemController {
  private final InventoryItemService service;

  public InventoryItemController(InventoryItemService service) {
    this.service = service;
  }

  @GetMapping
  @Operation(summary = "List inventory-items")
  public Page<InventoryItemResponse> list(@PageableDefault(size = 20) Pageable pageable) {
    return service.findAll(pageable);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a InventoryItem")
  public InventoryItemResponse get(@PathVariable UUID id) {
    return service.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create a InventoryItem")
  public InventoryItemResponse create(@Valid @RequestBody InventoryItemRequest request) {
    return service.create(request);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update a InventoryItem")
  public InventoryItemResponse update(
      @PathVariable UUID id, @Valid @RequestBody InventoryItemRequest request) {
    return service.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete a InventoryItem")
  public void delete(@PathVariable UUID id) {
    service.delete(id);
  }
}
