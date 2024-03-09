package com.example.ClothingStoreConfigServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ClothingStoreConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClothingStoreConfigServerApplication.class, args);
	}

}
