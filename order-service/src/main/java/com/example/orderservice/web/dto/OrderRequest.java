package com.example.orderservice.web.dto;

public record OrderRequest(
    @jakarta.validation.constraints.NotNull java.util.UUID userId,
    @jakarta.validation.constraints.NotNull @jakarta.validation.constraints.DecimalMin("0.00")
        java.math.BigDecimal total,
    @jakarta.validation.constraints.NotNull com.example.orderservice.domain.OrderStatus status) {}
