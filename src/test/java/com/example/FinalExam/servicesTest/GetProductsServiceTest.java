package com.example.FinalExam.servicesTest;

import com.example.FinalExam.category.Category;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.Product;
import com.example.FinalExam.product.model.ProductDTO;
import com.example.FinalExam.product.model.Region;
import com.example.FinalExam.product.services.GetProductsService;
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
public class GetProductsServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private GetProductsService getProductsService;

    private Pageable pageable;
    private Page<Product> productPage;
    private Product product;

    @BeforeEach
    void setUp() {
        // Sample product for testing
        Category category = new Category();
        category.setId(1);
        category.setName("Electronics");

        product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Test Product");
        product.setDescription("This is a test product description with more than 20 characters");
        product.setPrice(99.99);
        product.setManufacturer("Test Manufacturer");
        product.setCategory(category);
        product.setRegion(Region.US);

        // Creating a Pageable object for testing
        pageable = PageRequest.of(0, 10, Sort.by("name"));

        // Wrapping the product in a Page
        productPage = new PageImpl<>(Collections.singletonList(product), pageable, 1);
    }

    @Test
    void shouldReturnPageOfProductDTOs_WhenProductsExist() {
        // Arrange: Mocking the repository call to return a page of products
        when(productRepository.findAll(pageable)).thenReturn(productPage);

        // Act: Calling the service method
        PageResponse<ProductDTO> result = getProductsService.execute(pageable);

        // Assert: Verify the interaction with the repository and assert the result
        assertEquals(1, result.getCurrentPageNumberOfElements(), "Expected 1 product in the page");
        ProductTestUtils.assertProductMatchesDTO(product, result.getContent().get(0));
        verify(productRepository, times(1)).findAll(pageable);  // Verify the repository call was made
    }

    @Test
    void shouldReturnEmptyPage_WhenNoProductsExist() {
        // Arrange: Mocking the repository call to return an empty page
        Page<Product> emptyProductPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(productRepository.findAll(pageable)).thenReturn(emptyProductPage);

        // Act: Calling the service method
        PageResponse<ProductDTO> result = getProductsService.execute(pageable);

        // Assert: Verify that the result is empty
        assertTrue(result.getContent().isEmpty(), "Expected empty page of ProductDTOs");
        verify(productRepository, times(1)).findAll(pageable);  // Verify the repository call was made
    }
}
