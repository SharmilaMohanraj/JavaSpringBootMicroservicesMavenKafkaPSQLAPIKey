package com.example.orderservice.web.dto;

public record CouponApplicationRequest(
    @jakarta.validation.constraints.NotNull java.util.UUID orderId,
    @jakarta.validation.constraints.NotBlank String couponCode,
    @jakarta.validation.constraints.NotNull @jakarta.validation.constraints.DecimalMin("0.00")
        java.math.BigDecimal discountAmount) {}
