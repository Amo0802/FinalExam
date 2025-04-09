package com.example.FinalExam.product.services;

import com.example.FinalExam.common.Command;
import com.example.FinalExam.exceptions.ProductNotFoundException;
import com.example.FinalExam.product.model.Product;
import com.example.FinalExam.product.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeleteProductService implements Command<UUID, Void> {

    private final ProductRepository productRepository;

    public DeleteProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<Void> execute(UUID id) {
        Optional<Product> productWeWantToDelete = productRepository.findById(id);
        if(productWeWantToDelete.isPresent()){
            productRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

//      return ResponseEntity.notFound().build();
        throw new ProductNotFoundException();
    }
}
