package com.clothingstore.ClothingStoreResourceServer.controllers;

import com.clothingstore.ClothingStoreResourceServer.exceptions.AmountException;
import com.clothingstore.ClothingStoreResourceServer.exceptions.ExceptionModel;
import com.clothingstore.ClothingStoreResourceServer.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class AdviceController {
    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionModel httpClientErrorHandler(HttpClientErrorException e) {
        return new ExceptionModel(e.getMessage());
    }

    @ExceptionHandler(AmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionModel amountExceptionHandler(AmountException e) {
        return new ExceptionModel(e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionModel resourceNotFoundHandler(ResourceNotFoundException e) {
        return new ExceptionModel(e.getMessage());
    }
}
