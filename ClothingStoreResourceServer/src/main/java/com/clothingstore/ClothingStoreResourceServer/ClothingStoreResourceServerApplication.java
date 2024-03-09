package com.clothingstore.ClothingStoreResourceServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ClothingStoreResourceServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClothingStoreResourceServerApplication.class, args);
	}

}
