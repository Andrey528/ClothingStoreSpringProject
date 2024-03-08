package com.example.ClothingStoreGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ClothingStoreGateway {

	public static void main(String[] args) {
		SpringApplication.run(ClothingStoreGateway.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("resource-server", r -> r.path("/product/**")
						.uri("http://localhost:8081/"))
				.route("payment-server", r -> r.path("/**")
						.uri("http://localhost:8082/")).build();
	}
}
