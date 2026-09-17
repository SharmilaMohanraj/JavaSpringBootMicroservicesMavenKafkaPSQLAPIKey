package com.example.orderservice.web.dto;

import java.util.UUID;

public record PaymentRecordResponse(
    UUID id, java.util.UUID orderId, java.math.BigDecimal amount, String status) {}
