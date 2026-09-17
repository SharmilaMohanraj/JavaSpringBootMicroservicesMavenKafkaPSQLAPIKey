package com.example.userservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiKeyControllerIntegrationTest {
  private static final String ADMIN_KEY = "test-admin-key";
  @Autowired TestRestTemplate rest;
  @Autowired ObjectMapper mapper;

  @Test
  void createsListsAndDeletesApiKey() throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-Admin-Key", ADMIN_KEY);
    ResponseEntity<String> created =
        rest.exchange(
            "/api/v1/api-keys",
            HttpMethod.POST,
            new HttpEntity<>(Map.of("name", "test-" + System.nanoTime(), "role", "USER"), headers),
            String.class);
    assertEquals(HttpStatus.CREATED, created.getStatusCode());
    String id = mapper.readTree(created.getBody()).get("id").asText();
    assertEquals(
        HttpStatus.OK,
        rest.exchange("/api/v1/api-keys", HttpMethod.GET, new HttpEntity<>(headers), String.class)
            .getStatusCode());
    assertEquals(
        HttpStatus.NO_CONTENT,
        rest.exchange(
                "/api/v1/api-keys/" + id,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class)
            .getStatusCode());
  }
}
