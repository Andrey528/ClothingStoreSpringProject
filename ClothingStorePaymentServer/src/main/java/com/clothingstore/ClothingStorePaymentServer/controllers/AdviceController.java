package com.clothingstore.ClothingStorePaymentServer.controllers;

import com.clothingstore.ClothingStorePaymentServer.exceptions.BalanceException;
import com.clothingstore.ClothingStorePaymentServer.exceptions.ExceptionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceController {
    @ExceptionHandler(BalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionModel balanceExceptionHandler(BalanceException e) {
        return new ExceptionModel(e.getMessage());
    }
}
