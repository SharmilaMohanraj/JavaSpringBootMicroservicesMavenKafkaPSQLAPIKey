package com.example.orderservice.web.dto;

import java.util.UUID;

public record OrderResponse(
    UUID id,
    java.util.UUID userId,
    java.math.BigDecimal total,
    com.example.orderservice.domain.OrderStatus status) {}
