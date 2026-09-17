package com.example.inventoryservice.repository;

import com.example.inventoryservice.domain.InventoryItem;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, UUID> {}
