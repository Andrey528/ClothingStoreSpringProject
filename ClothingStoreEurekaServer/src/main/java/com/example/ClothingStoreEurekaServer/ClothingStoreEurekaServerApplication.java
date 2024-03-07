package com.example.ClothingStoreEurekaServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ClothingStoreEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClothingStoreEurekaServerApplication.class, args);
	}

}
