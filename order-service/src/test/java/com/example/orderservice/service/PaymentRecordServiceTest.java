package com.example.orderservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.orderservice.domain.PaymentRecord;
import com.example.orderservice.repository.PaymentRecordRepository;
import com.example.orderservice.web.dto.PaymentRecordRequest;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class PaymentRecordServiceTest {
  @Test
  void createMapsRequestAndSavesPaymentRecord() {
    PaymentRecordRepository repository = mock(PaymentRecordRepository.class);
    when(repository.save(any(PaymentRecord.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    var response =
        new PaymentRecordService(repository)
            .create(new PaymentRecordRequest(UUID.randomUUID(), new BigDecimal("25.00"), "PAID"));
    ArgumentCaptor<PaymentRecord> payment = ArgumentCaptor.forClass(PaymentRecord.class);
    verify(repository).save(payment.capture());
    assertEquals("PAID", payment.getValue().getStatus());
    assertEquals(new BigDecimal("25.00"), response.amount());
  }
}
