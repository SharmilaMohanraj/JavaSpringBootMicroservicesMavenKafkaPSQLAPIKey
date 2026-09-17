package com.example.catalogservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.catalogservice.domain.Product;
import com.example.catalogservice.repository.ProductRepository;
import com.example.catalogservice.web.dto.ProductRequest;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class ProductServiceTest {
  @Test
  void createMapsRequestAndSavesProduct() {
    ProductRepository repository = mock(ProductRepository.class);
    when(repository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
    var response =
        new ProductService(repository)
            .create(
                new ProductRequest(
                    "SKU-1", "Widget", "Useful", new BigDecimal("12.50"), "tools", true));
    ArgumentCaptor<Product> product = ArgumentCaptor.forClass(Product.class);
    verify(repository).save(product.capture());
    assertEquals("SKU-1", product.getValue().getSku());
    assertEquals(new BigDecimal("12.50"), response.price());
  }
}
