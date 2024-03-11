package com.clothingstore.ClothingStoreResourceServer.models.api;


import com.clothingstore.ClothingStoreResourceServer.dtos.PaymentInvoice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "payment")
public interface PaymentApi {
    @PostMapping()
    ResponseEntity<?> startPayingProcess(@RequestBody PaymentInvoice paymentInvoice);

    @PostMapping("/rollback")
    void rollbackPayingProcess(@RequestBody PaymentInvoice paymentInvoice);
}
