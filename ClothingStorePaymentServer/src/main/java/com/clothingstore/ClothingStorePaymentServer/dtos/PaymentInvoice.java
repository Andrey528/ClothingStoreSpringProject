package com.clothingstore.ClothingStorePaymentServer.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentInvoice {
    private String customerUsername;
    private BigDecimal sum;
}