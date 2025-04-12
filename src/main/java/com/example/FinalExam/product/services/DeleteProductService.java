package com.example.FinalExam.product.services;

import com.example.FinalExam.common.Command;
import com.example.FinalExam.exceptions.ProductNotFoundException;
import com.example.FinalExam.product.model.Product;
import com.example.FinalExam.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeleteProductService implements Command<UUID, Void> {

    private static final Logger logger = LoggerFactory.getLogger(DeleteProductService.class);

    private final ProductRepository productRepository;

    public DeleteProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public Void execute(UUID id) {
        logger.debug("Fetching product for deleting with ID: {}", id);
        Optional<Product> productWeWantToDelete = productRepository.findById(id);
        if(productWeWantToDelete.isPresent()){
            productRepository.deleteById(id);
            logger.info("Product deleted from DB: {}", productWeWantToDelete.get().getName());
            return null;
        }

        throw new ProductNotFoundException();
    }
}
