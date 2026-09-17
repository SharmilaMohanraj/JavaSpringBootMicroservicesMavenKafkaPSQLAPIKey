package com.example.orderservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.web.dto.OrderRequest;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class OrderServiceTest {
  @Test
  void createMapsRequestAndSavesOrder() {
    OrderRepository repository = mock(OrderRepository.class);
    when(repository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
    var response =
        new OrderService(repository)
            .create(
                new OrderRequest(UUID.randomUUID(), new BigDecimal("25.00"), OrderStatus.PENDING));
    ArgumentCaptor<Order> order = ArgumentCaptor.forClass(Order.class);
    verify(repository).save(order.capture());
    assertEquals(OrderStatus.PENDING, order.getValue().getStatus());
    assertEquals(new BigDecimal("25.00"), response.total());
  }
}
