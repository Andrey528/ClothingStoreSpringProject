package com.clothingstore.ClothingStoreResourceServer.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Data
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "img_src", nullable = false)
    private String imgSrc;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "color")
    private String color;

    @Column(name = "size")
    private String size;

    @Column(name = "reserved_quantity")
    private int reservedQuantity;

    @Column(name = "store_amount")
    private int storeAmount;
}
