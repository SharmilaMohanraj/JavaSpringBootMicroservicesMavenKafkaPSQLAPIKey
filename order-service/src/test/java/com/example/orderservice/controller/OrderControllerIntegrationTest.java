package com.example.orderservice.controller;

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
class OrderControllerIntegrationTest {
  @Autowired TestRestTemplate rest;
  @Autowired ObjectMapper mapper;

  @Test
  void createsReadsAndDeletesOrder() throws Exception {
    ResponseEntity<String> created =
        rest.postForEntity(
            "/api/v1/orders",
            Map.of("userId", UUID.randomUUID(), "total", 25.00, "status", "PENDING"),
            String.class);
    assertEquals(HttpStatus.CREATED, created.getStatusCode());
    String id = mapper.readTree(created.getBody()).get("id").asText();
    assertEquals(
        HttpStatus.OK, rest.getForEntity("/api/v1/orders/" + id, String.class).getStatusCode());
    assertEquals(
        HttpStatus.NO_CONTENT,
        rest.exchange("/api/v1/orders/" + id, HttpMethod.DELETE, null, String.class)
            .getStatusCode());
  }
}
