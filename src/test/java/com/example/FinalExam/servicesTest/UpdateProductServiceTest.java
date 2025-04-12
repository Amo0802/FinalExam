package com.example.FinalExam.servicesTest;

import com.example.FinalExam.category.Category;
import com.example.FinalExam.exceptions.ProductNotFoundException;
import com.example.FinalExam.exceptions.ProductNotValidException;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.Product;
import com.example.FinalExam.product.model.ProductDTO;
import com.example.FinalExam.product.model.Region;
import com.example.FinalExam.product.model.UpdateProductCommand;
import com.example.FinalExam.product.services.UpdateProductService;
import com.example.FinalExam.testUtils.ProductTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    UpdateProductService updateProductService;

    private Product product;
    private UUID productId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();

        Category category = new Category();
        category.setId(1);
        category.setName("Electronics");

        product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setDescription("Test description");
        product.setPrice(99.99);
        product.setManufacturer("Test Manufacturer");
        product.setCategory(category);
        product.setRegion(Region.US);
    }

    @Test
    void shouldReturnUpdatedProductDTO_WhenProductExists() {
        // Arrange
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated description that is longer than 20 characters");
        updatedProduct.setPrice(199.99);
        updatedProduct.setManufacturer("Updated Manufacturer");
        updatedProduct.setRegion(Region.CAN);
        updatedProduct.setCategory(product.getCategory());

        UpdateProductCommand command = new UpdateProductCommand(productId, updatedProduct);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // Act
        ProductDTO result = updateProductService.execute(command);

        // Assert
        ProductTestUtils.assertProductMatchesDTO(updatedProduct, result);
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldThrowException_WhenProductIsInvalid() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product invalidProduct = new Product();
        invalidProduct.setName(""); // invalid name
        invalidProduct.setDescription("Too short"); // invalid description
        invalidProduct.setPrice(-5.0); // invalid price
        invalidProduct.setManufacturer(""); // invalid manufacturer
        invalidProduct.setRegion(null); // invalid region

        UpdateProductCommand command = new UpdateProductCommand(productId, invalidProduct);

        // Act & Assert
        assertThrows(ProductNotValidException.class, () -> updateProductService.execute(command));

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any()); // ensure it doesn't save
    }

    @Test
    void shouldThrowException_WhenProductNotFound() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> updateProductService.execute(new UpdateProductCommand(productId, new Product())));
        verify(productRepository, times(1)).findById(productId);
    }
}
