package com.example.userservice.security;

import com.example.userservice.domain.ApiKey;
import com.example.userservice.repository.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {
  private static final String API_KEY_HEADER = "X-API-Key";
  private final ApiKeyRepository apiKeyRepository;

  public ApiKeyFilter(ApiKeyRepository apiKeyRepository) {
    this.apiKeyRepository = apiKeyRepository;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    return path.startsWith("/actuator/health")
        || path.equals("/docs")
        || path.startsWith("/docs/")
        || path.startsWith("/swagger-ui/")
        || path.startsWith("/api-docs")
        || path.startsWith("/v3/api-docs")
        || path.startsWith("/api/v1/api-keys");
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String rawKey = request.getHeader(API_KEY_HEADER);
    if (rawKey == null || rawKey.isBlank()) {
      unauthorized(response);
      return;
    }

    Optional<ApiKey> apiKey = apiKeyRepository.findByKeyHash(sha256(rawKey));
    if (apiKey.isEmpty() || !apiKey.get().getExpiresAt().isAfter(Instant.now())) {
      unauthorized(response);
      return;
    }

    ApiKey key = apiKey.get();
    var authentication =
        new UsernamePasswordAuthenticationToken(
            key.getId().toString(),
            null,
            List.of(new SimpleGrantedAuthority("ROLE_" + key.getRole().name())));
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
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

  private static void unauthorized(HttpServletResponse response) throws IOException {
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "A valid X-API-Key header is required");
  }
}
