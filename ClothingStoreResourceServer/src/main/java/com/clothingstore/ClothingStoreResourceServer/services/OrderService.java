package com.clothingstore.ClothingStoreResourceServer.services;

import com.clothingstore.ClothingStoreResourceServer.dtos.PaymentInvoice;
import com.clothingstore.ClothingStoreResourceServer.exceptions.AmountException;
import com.clothingstore.ClothingStoreResourceServer.models.Order;
import com.clothingstore.ClothingStoreResourceServer.models.ProductInOrderDetails;
import com.clothingstore.ClothingStoreResourceServer.models.api.PaymentApi;
import com.clothingstore.ClothingStoreResourceServer.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

/**
 * Сервис оплаты покупки.
 */
@Service
@AllArgsConstructor
public class OrderService {
    /**
     * Репозиторий Jpa для операций над объектом заказа
     */
    @Autowired
    private final OrderRepository orderRepository;
    /**
     * Сервис обработки товаров
     */
    @Autowired
    private final ProductService productService;
    /**
     * Объект клиента Feigen для запросов к api оплаты.
     */
    @Autowired
    private final PaymentApi paymentApi;

    /**
     * Метод сохранения заказа
     * @param order заказ от клиента
     * @return сохраненный объект из БД
     */
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    /**
     * Метод создания объекта PaymentInvoice, который хранит в себе username пользователя и сумму оплаты
     * @param order заказ от клиента
     * @return PaymentInvoice(username пользователя, сумма к оплате)
     */
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

    /**
     * Метод отправляет PaymentInvoice серверу оплаты
     * @param paymentInvoice хранит в себе username пользователя и сумму оплаты
     */
    private ResponseEntity<?> payOrder(PaymentInvoice paymentInvoice) throws HttpClientErrorException {
        return paymentApi.startPayingProcess(paymentInvoice);
    }

    /**
     * Метод завершения оплаты клиента. Снимает резервацию продуктов и уменьшает остаток продуктов на складе
     * @param order заказ от клиента
     */
    private void finalizeOrderPayment(Order order) {
        order.getProductInOrderDetails().forEach(productInOrderDetails ->
                productService.reduceStoreAndReservedAmount(
                        productInOrderDetails.getProduct().getId(),
                        productInOrderDetails.getAmount()
                )
        );
    }

    /**
     * Откат оплаты товара, который вызывается у сервера оплаты
     * @param paymentInvoice username пользователя, сумма к оплате
     * @throws HttpClientErrorException
     */
    private void rollbackOrderPayment(PaymentInvoice paymentInvoice) throws HttpClientErrorException {
        paymentApi.rollbackPayingProcess(paymentInvoice);
    }

    /**
     * Метод резервирования продуктов из заказа
     * @param listOfProducts заказанные продукты
     * @return успешность операции
     */
    private boolean reserveProductsFromOrder(List<ProductInOrderDetails> listOfProducts) {
        boolean isSuccess = true;

        for (int i = 0; i < listOfProducts.size(); i++) {
            try {
                productService.productReservation(
                        listOfProducts.get(i).getProduct().getId(),
                        listOfProducts.get(i).getAmount()
                );
            } catch (AmountException e) {
                isSuccess = false;

                for (int j = i - 1; j >= 0; j--) {
                    productService.rollbackProductReservation(
                            listOfProducts.get(j).getProduct().getId(),
                            listOfProducts.get(j).getAmount()
                    );
                }
                break;
            }
        }

        return isSuccess;
    }

    /**
     * Метод старта процесса оплаты
     * @param order заказ от клиента
     */
    public void startOrderPayment(Order order) {
        // Резервируем продукты
        if(reserveProductsFromOrder(order.getProductInOrderDetails()))
        {
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
        } else throw new AmountException();
    }
}
