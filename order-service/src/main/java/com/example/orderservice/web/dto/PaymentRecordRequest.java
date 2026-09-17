package com.example.orderservice.web.dto;

public record PaymentRecordRequest(
    @jakarta.validation.constraints.NotNull java.util.UUID orderId,
    @jakarta.validation.constraints.NotNull @jakarta.validation.constraints.DecimalMin("0.00")
        java.math.BigDecimal amount,
    @jakarta.validation.constraints.NotBlank String status) {}
