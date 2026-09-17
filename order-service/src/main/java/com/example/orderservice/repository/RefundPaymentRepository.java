package com.example.orderservice.repository;

import com.example.orderservice.domain.RefundPayment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundPaymentRepository extends JpaRepository<RefundPayment, UUID> {}
