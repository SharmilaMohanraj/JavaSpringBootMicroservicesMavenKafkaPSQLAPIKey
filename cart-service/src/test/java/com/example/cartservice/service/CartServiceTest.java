package com.example.cartservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.cartservice.domain.Cart;
import com.example.cartservice.repository.CartRepository;
import com.example.cartservice.web.dto.CartRequest;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CartServiceTest {
  @Test
  void createMapsRequestAndSavesCart() {
    CartRepository repository = mock(CartRepository.class);
    when(repository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));
    UUID userId = UUID.randomUUID();
    var response = new CartService(repository).create(new CartRequest(userId, "OPEN"));
    ArgumentCaptor<Cart> cart = ArgumentCaptor.forClass(Cart.class);
    verify(repository).save(cart.capture());
    assertEquals(userId, cart.getValue().getUserId());
    assertEquals("OPEN", response.status());
  }
}
