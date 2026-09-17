package com.example.orderservice.repository;

import com.example.orderservice.domain.ReturnRequest;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, UUID> {
  Page<ReturnRequest> findByCustomerId(UUID customerId, Pageable pageable);
}
