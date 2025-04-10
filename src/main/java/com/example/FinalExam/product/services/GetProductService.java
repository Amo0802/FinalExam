package com.example.FinalExam.product.services;

import com.example.FinalExam.exceptions.ProductNotFoundException;
import com.example.FinalExam.product.model.Product;
import com.example.FinalExam.common.Query;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GetProductService implements Query<UUID, ProductDTO> {

    private static final Logger logger = LoggerFactory.getLogger(GetProductService.class);

    private final ProductRepository productRepository;

    public GetProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDTO execute(UUID id) {
        logger.debug("Fetching product with ID: {}", id);
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isPresent()){
            logger.info("Product retrieved from DB: {}", productOptional.get().getName());
            return new ProductDTO(productOptional.get());
        }
        throw new ProductNotFoundException();
    }
}
