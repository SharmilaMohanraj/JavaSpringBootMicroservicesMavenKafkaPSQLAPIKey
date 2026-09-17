package com.example.inventoryservice.web.dto;

import jakarta.validation.constraints.*;

public record InventoryItemRequest(
    @jakarta.validation.constraints.NotNull java.util.UUID productId,
    @jakarta.validation.constraints.PositiveOrZero int quantity,
    @jakarta.validation.constraints.PositiveOrZero int reservedQuantity) {}
