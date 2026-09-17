package com.example.catalogservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerIntegrationTest {
  @Autowired TestRestTemplate rest;
  @Autowired ObjectMapper mapper;

  @Test
  void createsReadsAndDeletesProduct() throws Exception {
    ResponseEntity<String> created =
        rest.postForEntity(
            "/api/v1/products",
            Map.of(
                "sku",
                "SKU-" + System.nanoTime(),
                "name",
                "Widget",
                "description",
                "Useful",
                "price",
                12.50,
                "category",
                "tools",
                "active",
                true),
            String.class);
    assertEquals(HttpStatus.CREATED, created.getStatusCode());
    String id = mapper.readTree(created.getBody()).get("id").asText();
    assertEquals(
        HttpStatus.OK, rest.getForEntity("/api/v1/products/" + id, String.class).getStatusCode());
    assertEquals(
        HttpStatus.NO_CONTENT,
        rest.exchange("/api/v1/products/" + id, HttpMethod.DELETE, null, String.class)
            .getStatusCode());
  }
}
