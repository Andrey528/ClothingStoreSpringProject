package com.clothingstore.ClothingStorePaymentServer.exceptions;

import org.springframework.stereotype.Component;

@Component
public class BalanceException extends RuntimeException {
    public BalanceException() {
        super("Недостаточно средств!");
    }
}
