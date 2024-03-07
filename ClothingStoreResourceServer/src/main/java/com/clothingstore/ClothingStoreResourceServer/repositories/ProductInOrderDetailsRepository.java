package com.clothingstore.ClothingStoreResourceServer.repositories;

import com.clothingstore.ClothingStoreResourceServer.models.ProductInOrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInOrderDetailsRepository extends JpaRepository<ProductInOrderDetails, Long> {
}
