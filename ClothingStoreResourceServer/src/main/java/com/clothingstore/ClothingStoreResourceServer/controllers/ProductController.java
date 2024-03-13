package com.clothingstore.ClothingStoreResourceServer.controllers;

import com.clothingstore.ClothingStoreResourceServer.models.Product;
import com.clothingstore.ClothingStoreResourceServer.services.ProductService;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.io.IOException;
import java.io.InputStream;

@RestController
@AllArgsConstructor
@RequestMapping("/product")
@CrossOrigin(origins = "http://localhost:8080")
public class ProductController {
    @Autowired
    private final ProductService productService;

    @Timed(value = "getAllProducts.time", description = "Time taken to get all products")
    @GetMapping("/getAllProducts")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok().body(productService.getAllProducts());
    }

    @Timed(value = "getProduct.time", description = "Time taken to get a product by id")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(productService.getProductById(id));
    }

    @Timed(value = "getProductImg.time", description = "Time taken to get product image by id")
    @GetMapping("/{id}/productimg")
    public ResponseEntity<byte[]> getProductImg(@PathVariable("id") Long id) {
        String fileName = "product" + id + ".jpg";

        Resource resource = new ClassPathResource("static/img/" + fileName);

        try (InputStream in = resource.getInputStream()) {
            byte[] image = in.readAllBytes();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        }
        catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
