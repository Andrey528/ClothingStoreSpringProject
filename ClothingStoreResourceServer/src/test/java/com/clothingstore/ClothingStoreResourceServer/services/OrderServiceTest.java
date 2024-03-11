package com.clothingstore.ClothingStoreResourceServer.services;

import com.clothingstore.ClothingStoreResourceServer.dtos.PaymentInvoice;
import com.clothingstore.ClothingStoreResourceServer.exceptions.AmountException;
import com.clothingstore.ClothingStoreResourceServer.models.Order;
import com.clothingstore.ClothingStoreResourceServer.models.Product;
import com.clothingstore.ClothingStoreResourceServer.models.ProductInOrderDetails;
import com.clothingstore.ClothingStoreResourceServer.models.api.PaymentApi;
import com.clothingstore.ClothingStoreResourceServer.repositories.OrderRepository;
import jakarta.ws.rs.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private PaymentApi paymentApi;

    @InjectMocks
    private OrderService orderService;

    /**
     * Метод тестирования успешной работы сервисов при оплате продуктов
     */
    @Test
    public void testOrderPaymentCorrect() {
        // Создаем тестовые данные
        Order order = new Order();
        order.setCustomerUsername("testUser");
        List<ProductInOrderDetails> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setId(1L);
        product1.setStoreAmount(10);
        product1.setPrice(new BigDecimal(55));
        ProductInOrderDetails productInOrder1 = new ProductInOrderDetails(order, product1, 2);
        products.add(productInOrder1);
        order.setProductInOrderDetails(products);

        Mockito.when(paymentApi.startPayingProcess(any(PaymentInvoice.class)))
                .thenReturn(ResponseEntity.ok().build());

        orderService.startOrderPayment(order);

        verify(paymentApi).startPayingProcess(any(PaymentInvoice.class));
        verify(productService).productReservation(anyLong(), anyInt());

        verify(productService, never()).rollbackProductReservation(anyLong(), anyInt());
        verify(paymentApi, never()).rollbackPayingProcess(any(PaymentInvoice.class));
    }

    /**
     * Метод тестирования возникновения исключения в сервисе резервирования продукта при заказе продукта
     */
    @Test
    public void testOrderReservationException() {
        // Создаем тестовые данные
        Order order = new Order();
        order.setCustomerUsername("testUser");
        List<ProductInOrderDetails> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setId(1L);
        product1.setStoreAmount(10);
        product1.setPrice(new BigDecimal(55));
        ProductInOrderDetails productInOrder1 = new ProductInOrderDetails(order, product1, 2);
        products.add(productInOrder1);
        Product product2 = new Product();
        product2.setId(2L);
        product2.setStoreAmount(1);
        product2.setPrice(new BigDecimal(55));
        ProductInOrderDetails productInOrder2 = new ProductInOrderDetails(order, product2, 2);
        products.add(productInOrder2);
        order.setProductInOrderDetails(products);


        Mockito.doThrow(new AmountException()).when(productService).productReservation(anyLong(), anyInt());

        assertThrows(AmountException.class, () -> orderService.startOrderPayment(order));

        verify(productService).productReservation(anyLong(), anyInt());
    }

    /**
     * Метод тестирования возникновения исключения в сервисе оплаты продуктов при заказе
     */
    @Test
    public void testOrderPaymentException() {
        // Создаем тестовые данные
        Order order = new Order();
        order.setCustomerUsername("testUser");
        List<ProductInOrderDetails> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setId(1L);
        product1.setStoreAmount(10);
        product1.setPrice(new BigDecimal(1000));
        ProductInOrderDetails productInOrder1 = new ProductInOrderDetails(order, product1, 2);
        products.add(productInOrder1);
        Product product2 = new Product();
        product2.setId(2L);
        product2.setStoreAmount(1);
        product2.setPrice(new BigDecimal(55));
        ProductInOrderDetails productInOrder2 = new ProductInOrderDetails(order, product2, 2);
        products.add(productInOrder2);
        order.setProductInOrderDetails(products);

        Mockito.doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST)).when(paymentApi).startPayingProcess(any(PaymentInvoice.class));
        assertThrows(HttpClientErrorException.class, () -> orderService.startOrderPayment(order));

        verify(productService, times(2)).productReservation(anyLong(), anyInt());
        verify(paymentApi).startPayingProcess(any(PaymentInvoice.class));
        verify(productService, times(2)).rollbackProductReservation(anyLong(), anyInt());
    }
}