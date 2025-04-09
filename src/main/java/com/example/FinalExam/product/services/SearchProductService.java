package com.example.FinalExam.product.services;

import com.example.FinalExam.common.Query;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.ProductDTO;
import com.example.FinalExam.product.model.SearchProductQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchProductService implements Query<SearchProductQuery, List<ProductDTO>> {

    private final ProductRepository productRepository;

    public SearchProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ResponseEntity<List<ProductDTO>> execute(SearchProductQuery input){
        return ResponseEntity.ok(productRepository.searchByNameOrDescription(input.getSearch(), input.getCategory(), input.getSort())
                .stream()
                .map(ProductDTO::new)
                .toList());
    }
}
