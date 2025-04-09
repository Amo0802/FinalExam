package com.example.FinalExam.product.services;

import com.example.FinalExam.common.Query;
import com.example.FinalExam.category.CategoryRepository;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.ProductDTO;
import com.example.FinalExam.product.model.SearchProductQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchProductByCategoryService implements Query<SearchProductQuery, List<ProductDTO>> {

    private final ProductRepository productRepository;

    public SearchProductByCategoryService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
    }

    public ResponseEntity<List<ProductDTO>> execute(SearchProductQuery searchProductQuery){

        return ResponseEntity.ok(productRepository.searchByCategory(searchProductQuery.getCategory(), searchProductQuery.getSort())
                .stream()
                .map(ProductDTO::new)
                .toList());
    }
}
