package com.example.FinalExam.product;

import com.example.FinalExam.product.model.Product;
import com.example.FinalExam.product.model.ProductDTO;
import com.example.FinalExam.product.model.SearchProductQuery;
import com.example.FinalExam.product.model.UpdateProductCommand;
import com.example.FinalExam.product.services.*;
import com.example.FinalExam.utils.PageResponse;
import com.example.FinalExam.utils.SortMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final CreateProductService createProductService;
    private final DeleteProductService deleteProductService;
    private final GetProductService getProductService;
    private final UpdateProductService updateProductService;
    private final SearchProductService searchProductService;
    private final SearchProductByCategoryService searchProductByCategoryService;
    private final GetProductsService getProductsService;

    public ProductController(CreateProductService createProductService, DeleteProductService deleteProductService, GetProductService getProductService, UpdateProductService updateProductService, SearchProductService searchProductService, SearchProductByCategoryService searchProductByCategoryService, GetProductsService getProductsService) {
        this.createProductService = createProductService;
        this.deleteProductService = deleteProductService;
        this.getProductService = getProductService;
        this.updateProductService = updateProductService;
        this.searchProductService = searchProductService;
        this.searchProductByCategoryService = searchProductByCategoryService;
        this.getProductsService = getProductsService;
    }

    @PostMapping("/product")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody Product product){
        logger.info("Request received to create product: {}", product.getName());
        ProductDTO response = createProductService.execute(product);
        logger.info("Product created with ID: {}", response.getId());
        URI location = URI.create("/products/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable UUID id){
        logger.info("Request received to get product with ID: {}", id);
        ProductDTO response = getProductService.execute(id);
        logger.info("Product found: {}", response.getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id){
        logger.info("Request received to delete product with ID: {}", id);
        deleteProductService.execute(id);
        logger.info("Product deleted with ID: {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        //return ResponseEntity.noContent().build();
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID id, @RequestBody Product product){
        logger.info("Request received to update product with ID: {}", id);
        ProductDTO response = updateProductService.execute(new UpdateProductCommand(id, product));
        logger.info("Product updated with ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/search")
    public ResponseEntity<PageResponse<ProductDTO>> searchProduct(
            @RequestParam String search,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String category,
            @RequestParam(required = false, defaultValue = "") String sortBy
    ){
        logger.info("Search request received - search: {}, category: {}, sortBy: {}", search, category, sortBy);
        Sort sort = SortMapper.map(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        PageResponse<ProductDTO> response = searchProductService.execute(new SearchProductQuery(search, category, pageable));
        logger.info("Search completed, found {} products", response.getContent().size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/category")
    public ResponseEntity<PageResponse<ProductDTO>> searchProductByCategory(
            @RequestParam String category,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String sortBy
    ){
        logger.info("Request received to search product by category - category: {}, sortBy: {}, page: {}", category, sortBy, page);
        Sort sort = SortMapper.map(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        PageResponse<ProductDTO> response = searchProductByCategoryService.execute(new SearchProductQuery(null, category, pageable));
        logger.info("Category search completed, found {} products", response.getContent().size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products")
    public ResponseEntity<PageResponse<ProductDTO>> getProducts(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String sortBy
    ) {
        logger.info("Request received to get products - page: {}, size: {}, sortBy: {}", page, size, sortBy);
        Sort sort = SortMapper.map(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        PageResponse<ProductDTO> response = getProductsService.execute(pageable);
        logger.info("Retrieved products page {} of size {}, total: {}", page, size, response.getTotalElements());
        return ResponseEntity.ok(response);
    }
}
