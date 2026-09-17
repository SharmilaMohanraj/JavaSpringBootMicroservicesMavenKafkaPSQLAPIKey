package com.example.orderservice.web;

import com.example.orderservice.service.PaymentRecordService;
import com.example.orderservice.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment-records")
@Tag(name = "PaymentRecords")
public class PaymentRecordController {
  private final PaymentRecordService service;

  public PaymentRecordController(PaymentRecordService service) {
    this.service = service;
  }

  @GetMapping
  @Operation(summary = "List payment-records")
  public Page<PaymentRecordResponse> list(@PageableDefault(size = 20) Pageable pageable) {
    return service.findAll(pageable);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a PaymentRecord")
  public PaymentRecordResponse get(@PathVariable UUID id) {
    return service.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create a PaymentRecord")
  public PaymentRecordResponse create(@Valid @RequestBody PaymentRecordRequest request) {
    return service.create(request);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update a PaymentRecord")
  public PaymentRecordResponse update(
      @PathVariable UUID id, @Valid @RequestBody PaymentRecordRequest request) {
    return service.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete a PaymentRecord")
  public void delete(@PathVariable UUID id) {
    service.delete(id);
  }
}
