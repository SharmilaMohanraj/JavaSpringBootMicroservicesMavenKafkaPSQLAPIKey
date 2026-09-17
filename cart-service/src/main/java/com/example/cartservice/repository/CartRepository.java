package com.example.cartservice.repository;

import com.example.cartservice.domain.Cart;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, UUID> {}
