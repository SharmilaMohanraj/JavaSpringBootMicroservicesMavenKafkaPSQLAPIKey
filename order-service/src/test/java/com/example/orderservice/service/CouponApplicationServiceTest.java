package com.example.orderservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.orderservice.domain.CouponApplication;
import com.example.orderservice.repository.CouponApplicationRepository;
import com.example.orderservice.web.dto.CouponApplicationRequest;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CouponApplicationServiceTest {
  @Test
  void createMapsRequestAndSavesCouponApplication() {
    CouponApplicationRepository repository = mock(CouponApplicationRepository.class);
    when(repository.save(any(CouponApplication.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    var response =
        new CouponApplicationService(repository)
            .create(
                new CouponApplicationRequest(UUID.randomUUID(), "SAVE10", new BigDecimal("10.00")));
    ArgumentCaptor<CouponApplication> coupon = ArgumentCaptor.forClass(CouponApplication.class);
    verify(repository).save(coupon.capture());
    assertEquals("SAVE10", coupon.getValue().getCouponCode());
    assertEquals(new BigDecimal("10.00"), response.discountAmount());
  }
}
