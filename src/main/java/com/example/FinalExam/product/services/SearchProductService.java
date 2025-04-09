package com.example.FinalExam.product.services;

import com.example.FinalExam.common.Query;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.ProductDTO;
import com.example.FinalExam.product.model.SearchProductQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchProductService implements Query<SearchProductQuery, List<ProductDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(SearchProductService.class);

    private final ProductRepository productRepository;

    public SearchProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ResponseEntity<List<ProductDTO>> execute(SearchProductQuery input){
        logger.debug("Searching products with text: '{}', category: '{}', sort: {}",
                input.getSearch(), input.getCategory(), input.getSort());

        List<ProductDTO> products = productRepository.searchByNameOrDescription(
                        input.getSearch(), input.getCategory(), input.getSort())
                .stream()
                .map(ProductDTO::new)
                .toList();

        logger.info("Search results: {} products found", products.size());
        return ResponseEntity.ok(products);
    }
}
