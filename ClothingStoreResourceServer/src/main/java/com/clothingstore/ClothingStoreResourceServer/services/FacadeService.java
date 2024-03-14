package com.clothingstore.ClothingStoreResourceServer.services;

import com.clothingstore.ClothingStoreResourceServer.exceptions.ResourceNotFoundException;
import com.clothingstore.ClothingStoreResourceServer.models.Order;
import com.clothingstore.ClothingStoreResourceServer.models.Product;
import com.clothingstore.ClothingStoreResourceServer.models.ProductInOrderDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacadeService {
    /**
     * Сервис интеграции
     */
    private final GatewayService gatewayService;

    /**
     * Сервис заказа
     */
    private final OrderService orderService;

    /**
     * Сервис деталей заказа
     */
    private final ProductInOrderDetailsService productInOrderDetailsService;

    /**
     * Сервис продукта
     */
    private final ProductService productService;

    /**
     * Имя лог файла, в который осуществляется запись через интеграцию
     */
    private final String FILE_NAME = "resource_server_log.csv";

    private String getTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm:ss");
        return dateTime.format(formatter);
    }

    private String dataStringCompiler(String msg) {
        return getTime() + " | "
                + msg + Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    /**
     * Метод сохранения заказа пользователя и его логирования
     * @param order
     * @return
     */
    public Order saveOrder(Order order) {
        gatewayService.writeToFile(FILE_NAME,
                dataStringCompiler("Пользователь: " + order.getCustomerUsername()
                        + " передал заказ методу сохранения: "));
        return orderService.saveOrder(order);
    }

    /**
     * Метод инициализации процесса оплаты заказа и его логирования
     * @param order
     */
    public void startOrderPayment(Order order) {
        gatewayService.writeToFile(FILE_NAME,
                dataStringCompiler("Пользователь: " + order.getCustomerUsername()
                        + " передал заказ методу инициализации процесса оплаты: "));
        orderService.startOrderPayment(order);
    }

    /**
     * Метод сохранения деталей заказа и его логирование
     * @param productInOrderDetails
     * @return
     */
    public ProductInOrderDetails saveProductInOrderDetails(ProductInOrderDetails productInOrderDetails) {
        gatewayService.writeToFile(FILE_NAME,
                dataStringCompiler("Пользователь: " + productInOrderDetails.getOrder().getCustomerUsername()
                        + " передал детали заказа методу сохранения деталей заказа: "
                ));
        return productInOrderDetailsService.saveProductInOrderDetails(productInOrderDetails);
    }

    /**
     * Метод получения всех продуктов и его логирование
     * @return
     */
    public List<Product> getAllProducts() {
        gatewayService.writeToFile(FILE_NAME,
                dataStringCompiler("Поступил запрос на получение всех продуктов," +
                        "вызван метод получения всех продуктов: "
                ));
        return productService.getAllProducts();
    }

    /**
     * Метод получения продукта по id и его логирование
     * @param id
     * @return
     */
    public Product getProductById(Long id) {
        gatewayService.writeToFile(FILE_NAME,
                dataStringCompiler("Поступил запрос на получение продукта по id," +
                        "вызван метод получения продукта по id: "
                ));
        return productService.getProductById(id);
    }

    /**
     * Метод сохранения продукта в БД и его логирование
     * @param product
     * @return
     */
    public Product saveProduct(Product product) {
        gatewayService.writeToFile(FILE_NAME,
                dataStringCompiler("Поступил запрос на сохранение продукта," +
                        "вызван метод сохранения продукта в БД: "
                ));
        return productService.saveProduct(product);
    }

    /**
     * Метод удаления продукта по id и его логирование
     * @param id
     */
    public void deleteProduct(Long id) {
        gatewayService.writeToFile(FILE_NAME,
                dataStringCompiler("Поступил запрос на удаление продукта," +
                        "вызван метод удаления продукта из БД: "
                ));
        productService.deleteProduct(id);
    }
}
