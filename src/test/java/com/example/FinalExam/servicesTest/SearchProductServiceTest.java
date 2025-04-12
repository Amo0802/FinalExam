package com.example.FinalExam.servicesTest;

import com.example.FinalExam.category.Category;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.Product;
import com.example.FinalExam.product.model.ProductDTO;
import com.example.FinalExam.product.model.Region;
import com.example.FinalExam.product.model.SearchProductQuery;
import com.example.FinalExam.product.services.SearchProductService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class SearchProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    SearchProductService searchProductService;

    private Pageable pageable;
    private Product product;

    @BeforeEach
    void setUp() {
        Category category = new Category();
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
    void shouldReturnSearchedPageOfProductDTOs_WhenProductsExist(){
        // Arrange
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product), pageable, 1);
        when(productRepository.searchByNameOrDescription(anyString(), anyString(), eq(pageable)))
                .thenReturn(productPage);

        // Act
        PageResponse<ProductDTO> result = searchProductService.execute(new SearchProductQuery("Test", "", pageable));

        // Assert
        assertEquals(1, result.getCurrentPageNumberOfElements(), "Expected one product in the result");
        ProductTestUtils.assertProductMatchesDTO(product, result.getContent().get(0));
        verify(productRepository, times(1)).searchByNameOrDescription(anyString(), anyString(), eq(pageable));
    }

    @Test
    void shouldReturnEmptyPage_WhenSearchedProductDoesNotExist() {
        // Arrange
        Page<Product> emptyProductPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(productRepository.searchByNameOrDescription(anyString(), anyString(), eq(pageable)))
                .thenReturn(emptyProductPage);

        // Act
        PageResponse<ProductDTO> result = searchProductService.execute(new SearchProductQuery("NonMatchingWord", "", pageable));

        // Assert
        assertTrue(result.getContent().isEmpty(), "Expected empty page of ProductDTOs");
        verify(productRepository, times(1)).searchByNameOrDescription(anyString(), anyString(), eq(pageable));  // Verify the repository call was made
    }
}