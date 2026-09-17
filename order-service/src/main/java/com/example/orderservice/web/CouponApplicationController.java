package com.example.orderservice.web;

import com.example.orderservice.service.CouponApplicationService;
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
@RequestMapping("/api/v1/coupon-applications")
@Tag(name = "CouponApplications")
public class CouponApplicationController {
  private final CouponApplicationService service;

  public CouponApplicationController(CouponApplicationService service) {
    this.service = service;
  }

  @GetMapping
  @Operation(summary = "List coupon-applications")
  public Page<CouponApplicationResponse> list(@PageableDefault(size = 20) Pageable pageable) {
    return service.findAll(pageable);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a CouponApplication")
  public CouponApplicationResponse get(@PathVariable UUID id) {
    return service.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create a CouponApplication")
  public CouponApplicationResponse create(@Valid @RequestBody CouponApplicationRequest request) {
    return service.create(request);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update a CouponApplication")
  public CouponApplicationResponse update(
      @PathVariable UUID id, @Valid @RequestBody CouponApplicationRequest request) {
    return service.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Delete a CouponApplication")
  public void delete(@PathVariable UUID id) {
    service.delete(id);
  }
}
