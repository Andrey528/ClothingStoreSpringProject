package com.clothingstore.ClothingStoreResourceServer.configs;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountedMetricConfiguration {
    @Bean
    public CountedAspect countedAspect(MeterRegistry meterRegistry) { return new CountedAspect(meterRegistry); }
}
