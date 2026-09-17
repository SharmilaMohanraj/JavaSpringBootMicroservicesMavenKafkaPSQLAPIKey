package com.example.inventoryservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryItemControllerIntegrationTest {
  @Autowired TestRestTemplate rest;
  @Autowired ObjectMapper mapper;

  @Test
  void createsReadsAndDeletesInventoryItem() throws Exception {
    ResponseEntity<String> created =
        rest.postForEntity(
            "/api/v1/inventory-items",
            Map.of("productId", UUID.randomUUID(), "quantity", 10, "reservedQuantity", 2),
            String.class);
    assertEquals(HttpStatus.CREATED, created.getStatusCode());
    String id = mapper.readTree(created.getBody()).get("id").asText();
    assertEquals(
        HttpStatus.OK,
        rest.getForEntity("/api/v1/inventory-items/" + id, String.class).getStatusCode());
    assertEquals(
        HttpStatus.NO_CONTENT,
        rest.exchange("/api/v1/inventory-items/" + id, HttpMethod.DELETE, null, String.class)
            .getStatusCode());
  }
}
