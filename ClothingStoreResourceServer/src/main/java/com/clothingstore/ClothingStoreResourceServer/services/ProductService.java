package com.clothingstore.ClothingStoreResourceServer.services;

import com.clothingstore.ClothingStoreResourceServer.exceptions.AmountException;
import com.clothingstore.ClothingStoreResourceServer.exceptions.ResourceNotFoundException;
import com.clothingstore.ClothingStoreResourceServer.models.Product;
import com.clothingstore.ClothingStoreResourceServer.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    @Autowired
    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) throws ResourceNotFoundException {
        return productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id)
        );
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public void reduceStoreAndReservedAmount(Long id, int userOrderAmount) throws AmountException {
        Product product = getProductById(id);

        if (userOrderAmount > product.getStoreAmount())
            throw new AmountException();

        product.setStoreAmount(product.getStoreAmount() - userOrderAmount);
        product.setReservedQuantity(product.getReservedQuantity() - userOrderAmount);
        productRepository.save(product);
    }

    @Transactional
    public void productReservation(Long id, int userOrderAmount) throws AmountException {
        Product product = getProductById(id);

        if (userOrderAmount > product.getStoreAmount())
            throw new AmountException();

        product.setReservedQuantity(userOrderAmount);
        productRepository.save(product);
    }

    @Transactional
    public void rollbackProductReservation(Long id, int userOrderAmount) {
        Product product = getProductById(id);
        product.setReservedQuantity(product.getReservedQuantity() - userOrderAmount);
        productRepository.save(product);
    }
}
