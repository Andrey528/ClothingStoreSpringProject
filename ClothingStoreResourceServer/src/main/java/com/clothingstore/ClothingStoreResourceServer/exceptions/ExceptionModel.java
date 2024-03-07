package com.clothingstore.ClothingStoreResourceServer.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExceptionModel {
    private String message;
    private LocalDateTime localDateTime;

    public ExceptionModel(String message) {
        this.message = message;
        this.localDateTime = LocalDateTime.now();
    }
}
