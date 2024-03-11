package com.clothingstore.ClothingStoreResourceServer.services;

import com.clothingstore.ClothingStoreResourceServer.models.ProductInOrderDetails;
import com.clothingstore.ClothingStoreResourceServer.repositories.ProductInOrderDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductInOrderDetailsService {
    @Autowired
    private final ProductInOrderDetailsRepository productInOrderDetailsRepository;

    /**
     * Метод сохранения деталей заказа в БД
     * @param productInOrderDetails хранит id продукта и его количество
     * @return
     */
    public ProductInOrderDetails saveProductInOrderDetails(ProductInOrderDetails productInOrderDetails) {
        return productInOrderDetailsRepository.save(productInOrderDetails);
    }
}
