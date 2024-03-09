package com.clothingstore.ClothingStoreResourceServer.services;

import com.clothingstore.ClothingStoreResourceServer.dtos.PaymentInvoice;
import com.clothingstore.ClothingStoreResourceServer.models.Order;
import com.clothingstore.ClothingStoreResourceServer.models.api.PaymentApi;
import com.clothingstore.ClothingStoreResourceServer.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class OrderService {
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final ProductService productService;
    private final PaymentApi paymentApi;

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    // Этот метод служит для создания объекта PaymentInvoice, который хранит в себе username пользователя и сумму оплаты
    private PaymentInvoice collectPaymentInvoiceFromOrder(Order order) {
        BigDecimal totalPrice = BigDecimal.valueOf(order.getProductInOrderDetails()
                .stream().mapToDouble(productInOrderDetails ->
                        productInOrderDetails.getProduct().getPrice().doubleValue() * productInOrderDetails.getAmount()
                ).sum());

        return new PaymentInvoice(
                order.getCustomerUsername(),
                totalPrice
        );

    }

    // Метод отправляет PaymentInvoice серверу оплаты
    private void payOrder(PaymentInvoice paymentInvoice) {
        RestTemplate template = new RestTemplate();
        template.postForEntity(paymentApi.getBasicUri(), paymentInvoice, PaymentInvoice.class);
    }

    // Метод завершения оплаты клиента. Снимает резервацию продуктов и уменьшает остаток продуктов на складе
    private void finalizeOrderPayment(Order order) {
        order.getProductInOrderDetails().forEach(productInOrderDetails ->
                productService.reduceStoreAndReservedAmount(
                        productInOrderDetails.getProduct().getId(),
                        productInOrderDetails.getAmount()
                )
        );
    }

    // Откат оплаты товара, который вызывается у сервера оплаты
    private void rollbackOrderPayment(PaymentInvoice paymentInvoice) {
        RestTemplate template = new RestTemplate();

        template.postForEntity(paymentApi.getBasicUri() + "rollback", paymentInvoice, PaymentInvoice.class);
    }

    // Метод старта процесса оплаты
    public void startOrderPayment(Order order) {
        System.out.println(paymentApi);
        // Резервируем продукты
        order.getProductInOrderDetails().forEach(productInOrderDetails ->
                productService.productReservation(
                        productInOrderDetails.getProduct().getId(),
                        productInOrderDetails.getAmount()
                )
        );

        // Попробуй оплатить заказ
        try {
            // Собираем данные о пользователе(username) и деньгах(totalPrice), отправляем серверу оплаты
            payOrder(collectPaymentInvoiceFromOrder(order));
            // Пробуем зафиналить покупки, уменьшая остаток товаров на складе
            try {
                finalizeOrderPayment(order);
            }
            // Если не получается зафиналить покупки
            catch (HttpClientErrorException e) {
                // Откатываем оплату
                rollbackOrderPayment(collectPaymentInvoiceFromOrder(order));
                // Откатываем резервирование
                order.getProductInOrderDetails().forEach(productInOrderDetails ->
                        productService.rollbackProductReservation(
                                productInOrderDetails.getProduct().getId(),
                                productInOrderDetails.getAmount()
                        )
                );
            }
        }
        // Если оплатить не вышло
        catch (HttpClientErrorException e) {
            // Откатываем резервирование продуктов
            order.getProductInOrderDetails().forEach(productInOrderDetails ->
                    productService.rollbackProductReservation(
                            productInOrderDetails.getProduct().getId(),
                            productInOrderDetails.getAmount()
                    )
            );
            throw e;
        }
    }
}
