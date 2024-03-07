package com.clothingstore.ClothingStoreResourceServer.models.api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("api.payment")
public class PaymentApi {
    private String basicUri;
}
