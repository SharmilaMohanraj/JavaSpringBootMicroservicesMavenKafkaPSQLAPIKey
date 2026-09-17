package com.example.orderservice.web.dto;

import java.util.UUID;

public record CouponApplicationResponse(
    UUID id, java.util.UUID orderId, String couponCode, java.math.BigDecimal discountAmount) {}
