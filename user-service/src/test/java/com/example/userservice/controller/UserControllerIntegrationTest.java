package com.example.userservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {
  private static final String ADMIN_KEY = "test-admin-key";
  @Autowired TestRestTemplate rest;
  @Autowired ObjectMapper mapper;

  @Test
  void createsReadsAndDeletesUserWithPersistedApiKey() throws Exception {
    HttpHeaders adminHeaders = new HttpHeaders();
    adminHeaders.set("X-Admin-Key", ADMIN_KEY);
    ResponseEntity<String> keyCreated =
        rest.exchange(
            "/api/v1/api-keys",
            HttpMethod.POST,
            new HttpEntity<>(
                Map.of("name", "user-test-" + System.nanoTime(), "role", "USER"), adminHeaders),
            String.class);
    assertEquals(HttpStatus.CREATED, keyCreated.getStatusCode());
    JsonNode key = mapper.readTree(keyCreated.getBody());
    HttpHeaders userHeaders = new HttpHeaders();
    userHeaders.set("X-API-Key", key.get("apiKey").asText());
    ResponseEntity<String> created =
        rest.exchange(
            "/api/v1/users",
            HttpMethod.POST,
            new HttpEntity<>(
                Map.of(
                    "email",
                    "user-" + System.nanoTime() + "@example.test",
                    "displayName",
                    "Integration User",
                    "active",
                    true),
                userHeaders),
            String.class);
    assertEquals(HttpStatus.CREATED, created.getStatusCode());
    String id = mapper.readTree(created.getBody()).get("id").asText();
    assertEquals(
        HttpStatus.OK,
        rest.exchange(
                "/api/v1/users/" + id, HttpMethod.GET, new HttpEntity<>(userHeaders), String.class)
            .getStatusCode());
    assertEquals(
        HttpStatus.NO_CONTENT,
        rest.exchange(
                "/api/v1/users/" + id,
                HttpMethod.DELETE,
                new HttpEntity<>(userHeaders),
                String.class)
            .getStatusCode());
    rest.exchange(
        "/api/v1/api-keys/" + key.get("id").asText(),
        HttpMethod.DELETE,
        new HttpEntity<>(adminHeaders),
        String.class);
  }
}
