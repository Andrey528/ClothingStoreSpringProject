package com.clothingstore.ClothingStorePaymentServer.controllers;

import com.clothingstore.ClothingStorePaymentServer.dtos.PaymentInvoice;
import com.clothingstore.ClothingStorePaymentServer.services.PaymentService;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @Timed(value = "payment.transaction.time", description = "Time taken for payment transaction")
    @PostMapping()
    public ResponseEntity<Void> paymentPerformance(@RequestBody PaymentInvoice paymentInvoice) {
        paymentService.transaction(paymentInvoice);
        return ResponseEntity.ok(null);
    }

    @Timed(value = "payment.rollback.time", description = "Time taken for payment rollback transaction")
    @PostMapping("/rollback")
    public ResponseEntity<Void> rollbackPaymentPerformance(@RequestBody PaymentInvoice paymentInvoice){
        paymentService.rollbackTransaction(paymentInvoice);
        return ResponseEntity.ok().body(null);
    }
}
