package com.clothingstore.ClothingStoreResourceServer.exceptions;

import org.springframework.stereotype.Component;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Long id) {
        super("Товар " + id + " не найден!");
    }
}
