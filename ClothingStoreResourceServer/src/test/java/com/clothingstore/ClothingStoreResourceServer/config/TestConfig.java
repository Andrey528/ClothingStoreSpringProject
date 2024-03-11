package com.clothingstore.ClothingStoreResourceServer.config;

import com.clothingstore.ClothingStoreResourceServer.models.api.PaymentApi;
import lombok.RequiredArgsConstructor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
@RequiredArgsConstructor
public class TestConfig {
    @Bean
    @Primary
    public PaymentApi paymentApi() {
        return Mockito.mock(PaymentApi.class);
    }
}
