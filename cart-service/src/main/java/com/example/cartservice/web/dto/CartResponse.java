package com.example.cartservice.web.dto;

import java.util.UUID;

public record CartResponse(UUID id, java.util.UUID userId, String status) {}
