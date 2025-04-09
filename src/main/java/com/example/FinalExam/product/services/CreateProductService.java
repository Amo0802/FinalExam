package com.example.FinalExam.product.services;

import com.example.FinalExam.common.Command;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.Product;
import com.example.FinalExam.product.model.ProductDTO;
import com.example.FinalExam.product.validators.ProductValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class CreateProductService implements Command<Product, ProductDTO> {

    private static final Logger logger = LoggerFactory.getLogger(CreateProductService.class);

    private final ProductRepository productRepository;

    public CreateProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<ProductDTO> execute(Product input) {
        logger.debug("Validating product: {}", input.getName());
        ProductValidator.execute(input);
        logger.debug("Product validated: {}", input.getName());
        Product savedProduct = productRepository.save(input);
        logger.info("Product saved to DB: {}", savedProduct.getName());
        URI location = URI.create("/products/" + savedProduct.getId());
        return ResponseEntity.created(location).body(new ProductDTO(input));
    }
}
