package com.example.FinalExam;

import com.example.FinalExam.category.Category;
import com.example.FinalExam.product.ProductController;
import com.example.FinalExam.product.model.*;
import com.example.FinalExam.product.services.*;
import com.example.FinalExam.security.JwtUtil;
import com.example.FinalExam.utils.PageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateProductService createProductService;

    @MockitoBean
    private DeleteProductService deleteProductService;

    @MockitoBean
    private GetProductService getProductService;

    @MockitoBean
    private UpdateProductService updateProductService;

    @MockitoBean
    private SearchProductService searchProductService;

    @MockitoBean
    private SearchProductByCategoryService searchProductByCategoryService;

    @MockitoBean
    private GetProductsService getProductsService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private UUID productId;
    private Product product;
    private ProductDTO productDTO;
    private Category category;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();

        category = new Category();
        category.setId(1);
        category.setName("Electronics");

        product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setDescription("This is a test product description that is more than 20 characters");
        product.setPrice(99.99);
        product.setManufacturer("Test Manufacturer");
        product.setCategory(category);
        product.setRegion(Region.US);

        productDTO = new ProductDTO(product);
    }

    @Test
    void createProduct_Success() throws Exception {
        when(createProductService.execute(any(Product.class)))
                .thenReturn(productDTO);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(productId.toString())))
                .andExpect(jsonPath("$.name", is("Test Product")))
                .andExpect(jsonPath("$.price", is(99.99)));

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(createProductService).execute(productCaptor.capture());
        assertEquals("Test Product", productCaptor.getValue().getName());
    }

    @Test
    void getProduct_Success() throws Exception {
        when(getProductService.execute(productId))
                .thenReturn(productDTO);

        mockMvc.perform(get("/product/{id}", productId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId.toString())))
                .andExpect(jsonPath("$.name", is("Test Product")))
                .andExpect(jsonPath("$.description", is("This is a test product description that is more than 20 characters")))
                .andExpect(jsonPath("$.price", is(99.99)))
                .andExpect(jsonPath("$.manufacturer", is("Test Manufacturer")))
                .andExpect(jsonPath("$.category.name", is("Electronics")))
                .andExpect(jsonPath("$.region", is("US")));

        verify(getProductService).execute(productId);
    }

    @Test
    void deleteProduct_Success() throws Exception {
        doNothing().when(deleteProductService).execute(productId);

        mockMvc.perform(delete("/product/{id}", productId))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(deleteProductService).execute(productId);
    }

    @Test
    void updateProduct_Success() throws Exception {
        when(updateProductService.execute(any(UpdateProductCommand.class)))
                .thenReturn(productDTO);

        mockMvc.perform(put("/product/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId.toString())))
                .andExpect(jsonPath("$.name", is("Test Product")));

        ArgumentCaptor<UpdateProductCommand> commandCaptor = ArgumentCaptor.forClass(UpdateProductCommand.class);
        verify(updateProductService).execute(commandCaptor.capture());
        assertEquals(productId, commandCaptor.getValue().getId());
        assertEquals("Test Product", commandCaptor.getValue().getProduct().getName());
    }

    @Test
    void searchProduct_Success() throws Exception {
        PageResponse<ProductDTO> pageResponse = new PageResponse<>(Collections.singletonList(productDTO), 1, 1, 1, 1, 1, true);

        when(searchProductService.execute(any(SearchProductQuery.class)))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/product/search")
                        .param("search", "Test")
                        .param("category", "Electronics")
                        .param("sortBy", "price_asc")
                        .param("page", "1")
                        .param("size", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(productId.toString())))
                .andExpect(jsonPath("$.content[0].name", is("Test Product")))
                .andExpect(jsonPath("$.pageNumber", is(1)))
                .andExpect(jsonPath("$.pageSize", is(1)));

        ArgumentCaptor<SearchProductQuery> queryCaptor = ArgumentCaptor.forClass(SearchProductQuery.class);
        verify(searchProductService).execute(queryCaptor.capture());
        assertEquals("Test", queryCaptor.getValue().getSearch());
        assertEquals("Electronics", queryCaptor.getValue().getCategory());
        assertEquals(Sort.by("price").ascending(), queryCaptor.getValue().getPageable().getSort());
    }

    @Test
    void searchProductByCategory_Success() throws Exception {
        List<ProductDTO> productList = Arrays.asList(productDTO);
        Page<ProductDTO> pagedProducts = new PageImpl<>(productList, PageRequest.of(0, 10), 1);
        PageResponse<ProductDTO> pageResponse = new PageResponse<>(pagedProducts);

        when(searchProductByCategoryService.execute(any(SearchProductQuery.class)))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/product/category")
                        .param("category", "Electronics")
                        .param("sortBy", "name"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(productId.toString())))
                .andExpect(jsonPath("$.content[0].category.name", is("Electronics")));

        ArgumentCaptor<SearchProductQuery> queryCaptor = ArgumentCaptor.forClass(SearchProductQuery.class);
        verify(searchProductByCategoryService).execute(queryCaptor.capture());
        assertEquals("Electronics", queryCaptor.getValue().getCategory());
        assertEquals(Sort.by("name").ascending(), queryCaptor.getValue().getPageable().getSort());
    }

    @Test
    void getProducts_Success() throws Exception {
        List<ProductDTO> productList = Arrays.asList(productDTO);
        Page<ProductDTO> pagedProducts = new PageImpl<>(productList, PageRequest.of(0, 10), 1);
        PageResponse<ProductDTO> pageResponse = new PageResponse<>(pagedProducts);

        when(getProductsService.execute(any()))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/products")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name_desc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].id", is(productId.toString())))
                .andExpect(jsonPath("$.content[0].name", is("Test Product")));

        ArgumentCaptor<PageRequest> pageableCaptor = ArgumentCaptor.forClass(PageRequest.class);
        verify(getProductsService).execute(pageableCaptor.capture());
        assertEquals(0, pageableCaptor.getValue().getPageNumber());
        assertEquals(10, pageableCaptor.getValue().getPageSize());
        assertEquals(Sort.by("name").descending(), pageableCaptor.getValue().getSort());
    }

    @Test
    void getProducts_DefaultParameters() throws Exception {
        List<ProductDTO> productList = Arrays.asList(productDTO);
        Page<ProductDTO> pagedProducts = new PageImpl<>(productList, PageRequest.of(0, 10), 1);
        PageResponse<ProductDTO> pageResponse = new PageResponse<>(pagedProducts);

        when(getProductsService.execute(any()))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements", is(1)));

        ArgumentCaptor<PageRequest> pageableCaptor = ArgumentCaptor.forClass(PageRequest.class);
        verify(getProductsService).execute(pageableCaptor.capture());
        assertEquals(0, pageableCaptor.getValue().getPageNumber());
        assertEquals(10, pageableCaptor.getValue().getPageSize());
        assertEquals(Sort.unsorted(), pageableCaptor.getValue().getSort());
    }
}