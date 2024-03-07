package com.clothingstore.ClothingStoreResourceServer.exceptions;

public class AmountException extends RuntimeException {
    public AmountException () {
        super("Заказ превышает остаток на складе!");
    }
}
