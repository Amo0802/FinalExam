package com.example.FinalExam.product.services;

import com.example.FinalExam.common.Command;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.Product;
import com.example.FinalExam.product.model.ProductDTO;
import com.example.FinalExam.product.validators.ProductValidator;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class CreateProductService implements Command<Product, ProductDTO> {

    private final ProductRepository productRepository;

    public CreateProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<ProductDTO> execute(Product input) {
        ProductValidator.execute(input);
        Product savedProduct = productRepository.save(input);
        URI location = URI.create("/products/" + savedProduct.getId());
        return ResponseEntity.created(location).body(new ProductDTO(input));
    }
}
