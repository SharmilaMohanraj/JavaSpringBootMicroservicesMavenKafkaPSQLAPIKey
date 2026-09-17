package com.example.notificationservice;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI notificationServiceOpenAPI() {
    return new OpenAPI().addServersItem(new Server().url("/notification-service"));
  }
}
