package com.example.orderservice.repository;

import com.example.orderservice.domain.CouponApplication;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponApplicationRepository extends JpaRepository<CouponApplication, UUID> {}
