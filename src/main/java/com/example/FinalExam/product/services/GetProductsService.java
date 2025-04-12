package com.example.FinalExam.product.services;

import com.example.FinalExam.common.Query;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.ProductDTO;
import com.example.FinalExam.utils.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GetProductsService implements Query<Pageable, PageResponse<ProductDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(GetProductsService.class);

    private final ProductRepository productRepository;

    public GetProductsService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Cacheable(value = "products", key = "#pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    public PageResponse<ProductDTO> execute(Pageable pageable) {

        logger.debug("Fetching products with pageable: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        //.collect(Collectors.toList()) instead of .toList(), more control and mutable.
        Page<ProductDTO> page = productRepository.findAll(pageable).map(ProductDTO::new);

        logger.info("Retrieved {} products out of {} total",
                page.getNumberOfElements(), page.getTotalElements());

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
