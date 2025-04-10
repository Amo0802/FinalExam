package com.example.FinalExam.product.services;

import com.example.FinalExam.common.Query;
import com.example.FinalExam.category.CategoryRepository;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.ProductDTO;
import com.example.FinalExam.product.model.SearchProductQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchProductByCategoryService implements Query<SearchProductQuery, List<ProductDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(SearchProductByCategoryService.class);

    private final ProductRepository productRepository;

    public SearchProductByCategoryService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> execute(SearchProductQuery searchProductQuery){
        logger.debug("Searching products by category: '{}', sort: {}",
                searchProductQuery.getCategory(), searchProductQuery.getSort());

        List<ProductDTO> products = productRepository.searchByCategory(
                        searchProductQuery.getCategory(), searchProductQuery.getSort())
                .stream()
                .map(ProductDTO::new)
                .toList();

        logger.info("Category search results: {} products found in category '{}'",
                products.size(), searchProductQuery.getCategory());

        return products;
    }
}
