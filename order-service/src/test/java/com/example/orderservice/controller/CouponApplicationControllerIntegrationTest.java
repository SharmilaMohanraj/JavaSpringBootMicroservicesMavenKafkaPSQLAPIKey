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
class CouponApplicationControllerIntegrationTest {
  @Autowired TestRestTemplate rest;
  @Autowired ObjectMapper mapper;

  @Test
  void createsReadsAndDeletesCouponApplication() throws Exception {
    ResponseEntity<String> created =
        rest.postForEntity(
            "/api/v1/coupon-applications",
            Map.of("orderId", UUID.randomUUID(), "couponCode", "SAVE10", "discountAmount", 10.00),
            String.class);
    assertEquals(HttpStatus.CREATED, created.getStatusCode());
    String id = mapper.readTree(created.getBody()).get("id").asText();
    assertEquals(
        HttpStatus.OK,
        rest.getForEntity("/api/v1/coupon-applications/" + id, String.class).getStatusCode());
    assertEquals(
        HttpStatus.NO_CONTENT,
        rest.exchange("/api/v1/coupon-applications/" + id, HttpMethod.DELETE, null, String.class)
            .getStatusCode());
  }
}
