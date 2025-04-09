package com.example.FinalExam.product.services;

import com.example.FinalExam.common.Command;
import com.example.FinalExam.exceptions.ProductNotFoundException;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.Product;
import com.example.FinalExam.product.model.ProductDTO;
import com.example.FinalExam.product.model.UpdateProductCommand;
import com.example.FinalExam.product.validators.ProductValidator;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateProductService implements Command<UpdateProductCommand, ProductDTO> {

    private final ProductRepository productRepository;

    public UpdateProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<ProductDTO> execute(UpdateProductCommand input) {
        Optional<Product> productOptional = productRepository.findById(input.getId());

        if(productOptional.isPresent()){
            Product updatedProduct = input.getProduct();
            updatedProduct.setId(input.getId());
            ProductValidator.execute(updatedProduct);
            productRepository.save(updatedProduct);
            return ResponseEntity.ok(new ProductDTO(updatedProduct));
        }

//      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        throw new ProductNotFoundException();
    }
}
