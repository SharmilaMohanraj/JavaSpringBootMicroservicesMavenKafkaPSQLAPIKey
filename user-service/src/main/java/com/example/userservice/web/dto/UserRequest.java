package com.example.userservice.web.dto;

import jakarta.validation.constraints.*;

public record UserRequest(
    @jakarta.validation.constraints.Email @jakarta.validation.constraints.NotBlank String email,
    @jakarta.validation.constraints.NotBlank String displayName,
    boolean active) {}
