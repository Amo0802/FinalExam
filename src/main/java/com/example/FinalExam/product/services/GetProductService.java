package com.example.FinalExam.product.services;

import com.example.FinalExam.product.model.Product;
import com.example.FinalExam.common.Query;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.ProductDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GetProductService implements Query<UUID, ProductDTO> {

    private final ProductRepository productRepository;

    public GetProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ResponseEntity<ProductDTO> execute(UUID id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isPresent()){
            return ResponseEntity.ok(new ProductDTO(productOptional.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
