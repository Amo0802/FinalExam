package com.example.FinalExam.product;

import com.example.FinalExam.product.model.Product;
import com.example.FinalExam.product.model.ProductDTO;
import com.example.FinalExam.product.model.SearchProductQuery;
import com.example.FinalExam.product.model.UpdateProductCommand;
import com.example.FinalExam.product.services.*;
import com.example.FinalExam.utils.SortMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ProductController {

    private final CreateProductService createProductService;
    private final DeleteProductService deleteProductService;
    private final GetProductService getProductService;
    private final UpdateProductService updateProductService;
    private final SearchProductService searchProductService;
    private final SearchProductByCategoryService searchCategoryService;
    private final GetProductsService getProductsService;

    public ProductController(CreateProductService createProductService, DeleteProductService deleteProductService, GetProductService getProductService, UpdateProductService updateProductService, SearchProductService searchProductService, SearchProductByCategoryService searchCategoryService, GetProductsService getProductsService) {
        this.createProductService = createProductService;
        this.deleteProductService = deleteProductService;
        this.getProductService = getProductService;
        this.updateProductService = updateProductService;
        this.searchProductService = searchProductService;
        this.searchCategoryService = searchCategoryService;
        this.getProductsService = getProductsService;
    }

    @PostMapping("/product")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody Product product){
        return createProductService.execute(product);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable UUID id){
        return getProductService.execute(id);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id){
        return deleteProductService.execute(id);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID id, @RequestBody Product product){
        return updateProductService.execute(new UpdateProductCommand(id, product));
    }

    @GetMapping("/product/search")
    public ResponseEntity<List<ProductDTO>> searchProduct(
            @RequestParam String search,
            @RequestParam(required = false, defaultValue = "") String category,
            @RequestParam(required = false, defaultValue = "") String sortBy
    ){
        Sort sort = SortMapper.map(sortBy);
        return searchProductService.execute(new SearchProductQuery(search, category, sort));
    }

    @GetMapping("/product/category")
    public ResponseEntity<List<ProductDTO>> searchCategory(
            @RequestParam String category,
            @RequestParam(required = false, defaultValue = "") String sortBy
    ){
        Sort sort = SortMapper.map(sortBy);

        return searchCategoryService.execute(new SearchProductQuery(null, category, sort));
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductDTO>> getProducts(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String sortBy
    ) {
        Sort sort = SortMapper.map(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return getProductsService.execute(pageable);
    }
}
