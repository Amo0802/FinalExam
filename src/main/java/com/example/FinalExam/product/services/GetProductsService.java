package com.example.FinalExam.product.services;

import com.example.FinalExam.common.Query;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.ProductDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GetProductsService implements Query<Pageable, Page<ProductDTO>> {

    private final ProductRepository productRepository;

    public GetProductsService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Cacheable(value = "products", key = "#pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    public ResponseEntity<Page<ProductDTO>> execute(Pageable pageable) {

        //.collect(Collectors.toList()) instead of .toList(), more control and mutable.
        return ResponseEntity.ok(productRepository.findAll(pageable)
                .map(ProductDTO::new));
    }
}
