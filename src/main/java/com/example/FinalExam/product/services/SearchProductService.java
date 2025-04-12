package com.example.FinalExam.product.services;

import com.example.FinalExam.common.Query;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.ProductDTO;
import com.example.FinalExam.product.model.SearchProductQuery;
import com.example.FinalExam.utils.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class SearchProductService implements Query<SearchProductQuery, PageResponse<ProductDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(SearchProductService.class);

    private final ProductRepository productRepository;

    public SearchProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public PageResponse<ProductDTO> execute(SearchProductQuery input){
        logger.debug("Searching products with text: '{}', category: '{}', sort: {}, page: {}",
                input.getSearch(), input.getCategory(), input.getPageable().getSort(), input.getPageable());

        Page<ProductDTO> page = productRepository.searchByNameOrDescription(
                        input.getSearch(), input.getCategory(), input.getPageable())
                .map(ProductDTO::new);

        logger.info("Search results: {} products found", page.getContent().size());
        return new PageResponse<>(page);
    }
}
