package com.example.catalogservice.web.dto;

import java.util.UUID;

public record ProductResponse(
    UUID id,
    String sku,
    String name,
    String description,
    java.math.BigDecimal price,
    String category,
    boolean active) {}
