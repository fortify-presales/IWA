package com.microfocus.example.api.controllers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microfocus.example.entity.Product;
import com.microfocus.example.exception.ProductNotFoundException;
import com.microfocus.example.payload.request.ProductRequest;
import com.microfocus.example.service.ProductService;

import java.util.ArrayList;

import java.util.Optional;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ApiProductController.class})
@ExtendWith(SpringExtension.class)
class ApiProductControllerTest {
    @Autowired
    private ApiProductController apiProductController;

    @MockBean
    private ProductService productService;

    /**
     * Method under test: {@link ApiProductController#createProduct(ProductRequest)}
     */
    @Test
    void testCreateProduct() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setAvailable(true);
        productRequest.setCode("Code");
        productRequest.setDescription("The characteristics of someone or something");
        productRequest.setId(UUID.randomUUID());
        productRequest.setImage("Image");
        productRequest.setInStock(true);
        productRequest.setName("Name");
        productRequest.setOnSale(true);
        productRequest.setPrice(10.0f);
        productRequest.setRating(1);
        productRequest.setSalePrice(10.0f);
        productRequest.setSummary("Summary");
        productRequest.setTimeToStock(1);
        String content = (new ObjectMapper()).writeValueAsString(productRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v3/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiProductController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link ApiProductController#findProductById(UUID)}
     */
    @Test
    void testFindProductById() throws Exception {
        when(productService.findProductById((UUID) any())).thenThrow(new ProductNotFoundException("42"));
        when(productService.productExistsById((UUID) any())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/products/{id}",
                UUID.randomUUID());
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiProductController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiProductController#findProductById(UUID)}
     */
    @Test
    void testFindProductById2() throws Exception {
        when(productService.findProductById((UUID) any())).thenReturn(Optional.empty());
        when(productService.productExistsById((UUID) any())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/products/{id}",
                UUID.randomUUID());
        MockMvcBuilders.standaloneSetup(apiProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link ApiProductController#findProductById(UUID)}
     */
    @Test
    void testFindProductById3() throws Exception {
        Product product = new Product();
        product.setAvailable(true);
        product.setCode("Code");
        product.setDescription("The characteristics of someone or something");
        product.setId(UUID.randomUUID());
        product.setImage("Image");
        product.setInStock(true);
        product.setName("Name");
        product.setOnSale(true);
        product.setPrice(10.0f);
        product.setRating(1);
        product.setSalePrice(10.0f);
        product.setSummary("Summary");
        product.setTimeToStock(1);
        Optional<Product> ofResult = Optional.of(product);
        when(productService.findProductById((UUID) any())).thenReturn(ofResult);
        when(productService.productExistsById((UUID) any())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/products/{id}",
                UUID.randomUUID());
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiProductController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiProductController#createProduct(ProductRequest)}
     */
    @Test
    void testCreateProduct2() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setAvailable(true);
        productRequest.setCode("Code");
        productRequest.setDescription("The characteristics of someone or something");
        productRequest.setId(UUID.randomUUID());
        productRequest.setImage("Image");
        productRequest.setInStock(true);
        productRequest.setName("Name");
        productRequest.setOnSale(true);
        productRequest.setPrice(10.0f);
        productRequest.setRating(1);
        productRequest.setSalePrice(10.0f);
        productRequest.setSummary("Summary");
        productRequest.setTimeToStock(1);
        String content = (new ObjectMapper()).writeValueAsString(productRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v3/products", "Uri Vars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiProductController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link ApiProductController#deleteProduct(UUID)}
     */
    @Test
    void testDeleteProduct() throws Exception {
        doThrow(new ProductNotFoundException("42")).when(productService).deleteProductById((UUID) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v3/products/{id}",
                UUID.randomUUID());
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiProductController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiProductController#getProductsByKeywords(Optional, Optional, Optional)}
     */
    @Test
    void testGetProductsByKeywords() throws Exception {
        when(productService.getAllProducts((Integer) any(), (String) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v3/products");
        MockMvcBuilders.standaloneSetup(apiProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ApiProductController#getProductsByKeywords(Optional, Optional, Optional)}
     */
    @Test
    void testGetProductsByKeywords2() throws Exception {
        when(productService.getPageSize()).thenReturn(3);
        doNothing().when(productService).setPageSize((Integer) any());
        when(productService.getAllProducts((Integer) any(), (String) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/v3/products");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("limit", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(apiProductController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ApiProductController#getProductsByKeywords(Optional, Optional, Optional)}
     */
    @Test
    void testGetProductsByKeywords3() throws Exception {
        when(productService.getPageSize()).thenThrow(new ProductNotFoundException("42"));
        doThrow(new ProductNotFoundException("42")).when(productService).setPageSize((Integer) any());
        when(productService.getAllProducts((Integer) any(), (String) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/v3/products");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("limit", String.valueOf(1));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiProductController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link ApiProductController#updateProduct(ProductRequest, UUID)}
     */
    @Test
    void testUpdateProduct() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setAvailable(true);
        productRequest.setCode("Code");
        productRequest.setDescription("The characteristics of someone or something");
        productRequest.setId(UUID.randomUUID());
        productRequest.setImage("Image");
        productRequest.setInStock(true);
        productRequest.setName("Name");
        productRequest.setOnSale(true);
        productRequest.setPrice(10.0f);
        productRequest.setRating(1);
        productRequest.setSalePrice(10.0f);
        productRequest.setSummary("Summary");
        productRequest.setTimeToStock(1);
        String content = (new ObjectMapper()).writeValueAsString(productRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v3/products/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(apiProductController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}

