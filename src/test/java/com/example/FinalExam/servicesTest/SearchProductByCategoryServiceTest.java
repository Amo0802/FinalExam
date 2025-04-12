package com.example.FinalExam.servicesTest;

import com.example.FinalExam.category.Category;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.Product;
import com.example.FinalExam.product.model.ProductDTO;
import com.example.FinalExam.product.model.Region;
import com.example.FinalExam.product.model.SearchProductQuery;
import com.example.FinalExam.product.services.SearchProductByCategoryService;
import com.example.FinalExam.testUtils.ProductTestUtils;
import com.example.FinalExam.utils.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchProductByCategoryServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    SearchProductByCategoryService searchProductByCategoryService;

    private Product product;
    private Pageable pageable;
    private Category category;

    @BeforeEach
    void setUp(){
        category = new Category();
        category.setId(1);
        category.setName("CategoryName");

        product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Test Product");
        product.setDescription("This is a test product description with more than 20 characters");
        product.setPrice(99.99);
        product.setManufacturer("Test Manufacturer");
        product.setCategory(category);
        product.setRegion(Region.US);

        pageable = PageRequest.of(0, 10, Sort.by("name"));
    }

    @Test
    void shouldReturnPageOfProductDTOsFilteredByCategory_WhenProductsExist() {
        // Arrange
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product), pageable, 1);
        when(productRepository.searchByCategory(anyString(), eq(pageable)))
                .thenReturn(productPage);

        // Act
        PageResponse<ProductDTO> result = searchProductByCategoryService.execute(new SearchProductQuery("", "CategoryName", pageable));

        // Assert
        assertEquals(1, result.getCurrentPageNumberOfElements(), "Expected one product in the result");
        ProductTestUtils.assertProductMatchesDTO(product, result.getContent().get(0));
        verify(productRepository, times(1)).searchByCategory(anyString(), eq(pageable));
    }

    @Test
    void shouldReturnEmptyPage_WhenSearchedCategoryDoesNotExist() {
        //Arrange
        Page<Product> emptyProductPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(productRepository.searchByCategory(anyString(), eq(pageable))).thenReturn(emptyProductPage);

        //Act
        PageResponse<ProductDTO> result = searchProductByCategoryService.execute(new SearchProductQuery("", "NonExistingCategory", pageable));

        //Assert
        assertTrue(result.getContent().isEmpty(), "Expected empty page of ProductDTOs");
        verify(productRepository, times(1)).searchByCategory(anyString(), eq(pageable));
    }
}
