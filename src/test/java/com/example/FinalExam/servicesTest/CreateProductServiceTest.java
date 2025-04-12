package com.example.FinalExam.servicesTest;

import com.example.FinalExam.category.Category;
import com.example.FinalExam.exceptions.ProductNotValidException;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.Product;
import com.example.FinalExam.product.model.ProductDTO;
import com.example.FinalExam.product.model.Region;
import com.example.FinalExam.product.services.CreateProductService;
import com.example.FinalExam.testUtils.ProductTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CreateProductService createProductService;

    private Product product;

    @BeforeEach
    void setUp() {

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
    }

    @Test
    void shouldCreateProductSuccessfully_WhenProductIsValid() {
        // Given
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When
        ProductDTO response = createProductService.execute(product);

        // Then
        assertNotNull(response);
        ProductTestUtils.assertProductMatchesDTO(product, response);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldGenerateId_WhenIdIsNotProvided() {
        // Given
        product.setId(null);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When
        ProductDTO response = createProductService.execute(product);

        // Then
        assertNotNull(response.getId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldThrowException_WhenProductIsInvalid() {
        // Given
        product.setName("");

        // When/Then
        assertThrows(ProductNotValidException.class, () -> createProductService.execute(product));
        verify(productRepository, never()).save(any(Product.class));
    }
}