package com.clothingstore.ClothingStoreClient.configs;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountedConfiguration {
    @Bean
    public CountedAspect countedAspect(MeterRegistry meterRegistry) { return new CountedAspect(meterRegistry); }
}
