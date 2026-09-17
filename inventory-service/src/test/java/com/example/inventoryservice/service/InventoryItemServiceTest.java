package com.example.inventoryservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.inventoryservice.domain.InventoryItem;
import com.example.inventoryservice.repository.InventoryItemRepository;
import com.example.inventoryservice.web.dto.InventoryItemRequest;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class InventoryItemServiceTest {
  @Test
  void createMapsRequestAndSavesInventoryItem() {
    InventoryItemRepository repository = mock(InventoryItemRepository.class);
    when(repository.save(any(InventoryItem.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    UUID productId = UUID.randomUUID();
    var response =
        new InventoryItemService(repository).create(new InventoryItemRequest(productId, 10, 2));
    ArgumentCaptor<InventoryItem> item = ArgumentCaptor.forClass(InventoryItem.class);
    verify(repository).save(item.capture());
    assertEquals(productId, item.getValue().getProductId());
    assertEquals(2, response.reservedQuantity());
  }
}
