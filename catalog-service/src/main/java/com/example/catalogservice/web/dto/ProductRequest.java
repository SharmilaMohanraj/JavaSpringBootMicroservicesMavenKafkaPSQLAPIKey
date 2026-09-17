package com.example.catalogservice.web.dto;

import jakarta.validation.constraints.*;

public record ProductRequest(
    @jakarta.validation.constraints.NotBlank String sku,
    @jakarta.validation.constraints.NotBlank String name,
    String description,
    @jakarta.validation.constraints.NotNull @jakarta.validation.constraints.DecimalMin("0.00")
        java.math.BigDecimal price,
    String category,
    boolean active) {}
