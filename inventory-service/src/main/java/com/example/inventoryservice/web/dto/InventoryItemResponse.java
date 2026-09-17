package com.example.inventoryservice.web.dto;

import java.util.UUID;

public record InventoryItemResponse(
    UUID id, java.util.UUID productId, int quantity, int reservedQuantity) {}
