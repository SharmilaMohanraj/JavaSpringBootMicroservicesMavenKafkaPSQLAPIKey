package com.example.userservice.web;

import com.example.userservice.domain.ApiKey;
import com.example.userservice.repository.ApiKeyRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/api-keys")
public class ApiKeyController {
  private static final Duration KEY_LIFETIME = Duration.ofMinutes(30);
  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  private final ApiKeyRepository apiKeyRepository;
  private final byte[] adminApiKey;

  public ApiKeyController(
      ApiKeyRepository apiKeyRepository, @Value("${ADMIN_API_KEY}") String adminApiKey) {
    this.apiKeyRepository = apiKeyRepository;
    this.adminApiKey = adminApiKey.getBytes(StandardCharsets.UTF_8);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CreatedApiKey create(
      @RequestHeader(value = "X-Admin-Key", required = false) String suppliedKey,
      @Valid @RequestBody CreateApiKeyRequest request) {
    requireAdminKey(suppliedKey);
    String rawKey = generateKey();
    ApiKey key =
        apiKeyRepository.save(
            new ApiKey(
                request.name(), sha256(rawKey), request.role(), Instant.now().plus(KEY_LIFETIME)));
    return new CreatedApiKey(key.getId(), key.getName(), rawKey, key.getRole(), key.getExpiresAt());
  }

  @GetMapping
  public List<ApiKeyDetails> list(
      @RequestHeader(value = "X-Admin-Key", required = false) String suppliedKey) {
    requireAdminKey(suppliedKey);
    return apiKeyRepository.findAll().stream()
        .map(
            key ->
                new ApiKeyDetails(
                    key.getId(),
                    key.getName(),
                    key.getRole(),
                    key.getCreatedAt(),
                    key.getExpiresAt()))
        .toList();
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(
      @RequestHeader(value = "X-Admin-Key", required = false) String suppliedKey,
      @PathVariable UUID id) {
    requireAdminKey(suppliedKey);
    if (!apiKeyRepository.existsById(id)) {
      throw new ApiKeyNotFoundException();
    }
    apiKeyRepository.deleteById(id);
  }

  private void requireAdminKey(String suppliedKey) {
    byte[] supplied =
        suppliedKey == null ? new byte[0] : suppliedKey.getBytes(StandardCharsets.UTF_8);
    if (!MessageDigest.isEqual(adminApiKey, supplied)) {
      throw new AccessDeniedException("An administrator API key is required");
    }
  }

  private static String generateKey() {
    byte[] bytes = new byte[32];
    SECURE_RANDOM.nextBytes(bytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  private static String sha256(String value) {
    try {
      return HexFormat.of()
          .formatHex(
              MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));
    } catch (NoSuchAlgorithmException exception) {
      throw new IllegalStateException("SHA-256 is unavailable", exception);
    }
  }

  public record CreateApiKeyRequest(@NotBlank String name, @NotNull ApiKey.Role role) {}

  public record CreatedApiKey(
      UUID id, String name, String apiKey, ApiKey.Role role, Instant expiresAt) {}

  public record ApiKeyDetails(
      UUID id, String name, ApiKey.Role role, Instant createdAt, Instant expiresAt) {}

  @ResponseStatus(HttpStatus.NOT_FOUND)
  private static class ApiKeyNotFoundException extends RuntimeException {}
}
