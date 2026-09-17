package com.example.cartservice.web.dto;

import jakarta.validation.constraints.*;

public record CartRequest(
    @jakarta.validation.constraints.NotNull java.util.UUID userId,
    @jakarta.validation.constraints.NotBlank String status) {}
