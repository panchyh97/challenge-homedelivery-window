package com.challenge.homedelivery.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration 
public class OpenApiConfig {
  @Bean
  public OpenAPI openApi() {
    return new OpenAPI().info(new Info().title("Home Delivery API").version("1.0.0")
        .description("Delivery window booking with geographic coverage and capacity control."));
  }
}
