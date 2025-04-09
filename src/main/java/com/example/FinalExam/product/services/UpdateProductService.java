package com.example.FinalExam.product.services;

import com.example.FinalExam.common.Command;
import com.example.FinalExam.exceptions.ProductNotFoundException;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.Product;
import com.example.FinalExam.product.model.ProductDTO;
import com.example.FinalExam.product.model.UpdateProductCommand;
import com.example.FinalExam.product.validators.ProductValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UpdateProductService implements Command<UpdateProductCommand, ProductDTO> {

    private static final Logger logger = LoggerFactory.getLogger(UpdateProductService.class);

    private final ProductRepository productRepository;

    public UpdateProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<ProductDTO> execute(UpdateProductCommand input) {
        UUID id = input.getId();
        logger.debug("Fetching product with ID: {}", id);

        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product updatedProduct = input.getProduct();
            updatedProduct.setId(id);

            logger.debug("Validating product: {}", updatedProduct.getName());
            ProductValidator.execute(updatedProduct);
            logger.debug("Product validation passed: {}", updatedProduct.getName());

            productRepository.save(updatedProduct);
            logger.info("Product updated successfully: ID={} Name={}", id, updatedProduct.getName());

            return ResponseEntity.ok(new ProductDTO(updatedProduct));
        }

        throw new ProductNotFoundException();
    }
}
