package com.example.FinalExam.product.services;

import com.example.FinalExam.common.Query;
import com.example.FinalExam.category.CategoryRepository;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.ProductDTO;
import com.example.FinalExam.product.model.SearchProductQuery;
import com.example.FinalExam.utils.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class SearchProductByCategoryService implements Query<SearchProductQuery, PageResponse<ProductDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(SearchProductByCategoryService.class);

    private final ProductRepository productRepository;

    public SearchProductByCategoryService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
    }

    public PageResponse<ProductDTO> execute(SearchProductQuery searchProductQuery){
        logger.debug("Searching products by category: '{}', sort: {}, page: {}",
                searchProductQuery.getCategory(), searchProductQuery.getPageable().getSort(), searchProductQuery.getPageable());

        Page<ProductDTO> page = productRepository.searchByCategory(
                searchProductQuery.getCategory(), searchProductQuery.getPageable())
                .map(ProductDTO::new);

        logger.info("Category search results: {} products found in category '{}'",
                page.getContent().size(), searchProductQuery.getCategory());

        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumberOfElements(),
                page.isLast()
        );
    }
}
