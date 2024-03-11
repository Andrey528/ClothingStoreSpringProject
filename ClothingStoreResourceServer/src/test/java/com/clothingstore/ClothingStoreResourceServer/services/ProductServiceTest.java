package com.clothingstore.ClothingStoreResourceServer.services;

import com.clothingstore.ClothingStoreResourceServer.exceptions.AmountException;
import com.clothingstore.ClothingStoreResourceServer.exceptions.ResourceNotFoundException;
import com.clothingstore.ClothingStoreResourceServer.models.Product;
import com.clothingstore.ClothingStoreResourceServer.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void getAllProductTest() {
        List<Product> products = List.of(new Product());

        given(productRepository.findAll()).willReturn(products);

        List<Product> testListProduct = productService.getAllProducts();

        verify(productRepository).findAll();

        Assertions.assertEquals(products.size(), testListProduct.size());
    }

    @Test
    public void getProductByIdExpectProduct() {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));

        Product testProduct = productService.getProductById(productId);
        verify(productRepository).findById(productId);

        Assertions.assertEquals(product, testProduct);
    }

    @Test
    public void getProductByIdExpectException(){
        Long productId = 1L;
        given(productRepository.findById(productId))
                .willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                productService.getProductById(productId));

        verify(productRepository).findById(productId);
    }

    @Test
    public void reduceAmountExpectTrue(){
        Long productId = 1L;
        int amountInOrder = 3;
        int amountInStorage = 6;
        int amountInReserve = 2;
        Product product = new Product();

        product.setId(productId);
        product.setStoreAmount(amountInStorage);
        product.setReservedQuantity(amountInReserve);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));

        productService.reduceStoreAndReservedAmount(productId, amountInOrder);

        verify(productRepository).findById(productId);
        verify(productRepository).save(product);

        Assertions.assertEquals(amountInStorage - amountInOrder,
                product.getStoreAmount());
        Assertions.assertEquals(amountInReserve - amountInOrder,
                product.getReservedQuantity());
    }

    @Test
    public void reduceAmountExpectException(){
        Long productId= 1L;
        int amountInOrder = 5;
        int amountInStorage = 1;
        Product product = new Product();

        product.setId(productId);
        product.setStoreAmount(amountInStorage);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));

        assertThrows(AmountException.class,
                () -> productService.reduceStoreAndReservedAmount(productId, amountInOrder));

        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(product);
    }

    @Test
    public void reservedProductExpectTrue(){
        Long productId = 1L;
        int amountInOrder = 2;
        int amountInStorage = 5;
        Product product = new Product();

        product.setId(productId);
        product.setStoreAmount(amountInStorage);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));

        productService.productReservation(productId, amountInOrder);

        verify(productRepository).findById(productId);
        verify(productRepository).save(product);

        Assertions.assertEquals(amountInOrder, product.getReservedQuantity());
    }

    @Test
    public void reservedProductExpectException(){
        Long productId= 1L;
        int amountInOrder = 5;
        int amountInStorage = 1;
        Product product = new Product();

        product.setId(productId);
        product.setStoreAmount(amountInStorage);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));

        assertThrows(AmountException.class,
                () -> productService.productReservation(productId, amountInOrder));

        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(product);
    }

    @Test
    public void rollbackReservedProductTest(){
        Long productId = 1L;
        int amountInOrder = 1;
        int amountInReserved = 1;
        Product product = new Product();
        product.setId(productId);
        product.setReservedQuantity(amountInReserved);

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));

        productService.rollbackProductReservation(productId, amountInOrder);

        verify(productRepository).findById(productId);
        verify(productRepository).save(product);

        Assertions.assertEquals(amountInReserved - amountInOrder, product.getReservedQuantity());
    }
}