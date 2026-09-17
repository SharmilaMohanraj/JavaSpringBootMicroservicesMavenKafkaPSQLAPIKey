package com.example.orderservice.repository;

import com.example.orderservice.domain.PaymentRecord;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, UUID> {}
