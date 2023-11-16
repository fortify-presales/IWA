package com.microfocus.example.payload.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.microfocus.example.entity.Product;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class ProductRequestTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link ProductRequest#ProductRequest()}
     *   <li>{@link ProductRequest#setAvailable(Boolean)}
     *   <li>{@link ProductRequest#setCode(String)}
     *   <li>{@link ProductRequest#setDescription(String)}
     *   <li>{@link ProductRequest#setId(UUID)}
     *   <li>{@link ProductRequest#setImage(String)}
     *   <li>{@link ProductRequest#setInStock(Boolean)}
     *   <li>{@link ProductRequest#setName(String)}
     *   <li>{@link ProductRequest#setOnSale(Boolean)}
     *   <li>{@link ProductRequest#setPrice(float)}
     *   <li>{@link ProductRequest#setRating(int)}
     *   <li>{@link ProductRequest#setSalePrice(float)}
     *   <li>{@link ProductRequest#setSummary(String)}
     *   <li>{@link ProductRequest#setTimeToStock(int)}
     *   <li>{@link ProductRequest#toString()}
     *   <li>{@link ProductRequest#getAvailable()}
     *   <li>{@link ProductRequest#getCode()}
     *   <li>{@link ProductRequest#getDescription()}
     *   <li>{@link ProductRequest#getId()}
     *   <li>{@link ProductRequest#getImage()}
     *   <li>{@link ProductRequest#getInStock()}
     *   <li>{@link ProductRequest#getName()}
     *   <li>{@link ProductRequest#getOnSale()}
     *   <li>{@link ProductRequest#getPrice()}
     *   <li>{@link ProductRequest#getRating()}
     *   <li>{@link ProductRequest#getSalePrice()}
     *   <li>{@link ProductRequest#getSummary()}
     *   <li>{@link ProductRequest#getTimeToStock()}
     * </ul>
     */
    @Test
    void testConstructor() {
        ProductRequest actualProductRequest = new ProductRequest();
        actualProductRequest.setAvailable(true);
        actualProductRequest.setCode("Code");
        actualProductRequest.setDescription("The characteristics of someone or something");
        UUID randomUUIDResult = UUID.randomUUID();
        actualProductRequest.setId(randomUUIDResult);
        actualProductRequest.setImage("Image");
        actualProductRequest.setInStock(true);
        actualProductRequest.setName("Name");
        actualProductRequest.setOnSale(true);
        actualProductRequest.setPrice(10.0f);
        actualProductRequest.setRating(1);
        actualProductRequest.setSalePrice(10.0f);
        actualProductRequest.setSummary("Summary");
        actualProductRequest.setTimeToStock(1);
        String actualToStringResult = actualProductRequest.toString();
        assertTrue(actualProductRequest.getAvailable());
        assertEquals("Code", actualProductRequest.getCode());
        assertEquals("The characteristics of someone or something", actualProductRequest.getDescription());
        assertSame(randomUUIDResult, actualProductRequest.getId());
        assertEquals("Image", actualProductRequest.getImage());
        assertTrue(actualProductRequest.getInStock());
        assertEquals("Name", actualProductRequest.getName());
        assertTrue(actualProductRequest.getOnSale());
        assertEquals(10.0f, actualProductRequest.getPrice());
        assertEquals(1, actualProductRequest.getRating());
        assertEquals(10.0f, actualProductRequest.getSalePrice());
        assertEquals("Summary", actualProductRequest.getSummary());
        assertEquals(1, actualProductRequest.getTimeToStock());
        assertEquals("ProductRequest(Name : SRP : 10.0)", actualToStringResult);
    }

    /**
     * Method under test: {@link ProductRequest#ProductRequest(Product)}
     */
    @Test
    void testConstructor2() {
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
        ProductRequest actualProductRequest = new ProductRequest(product);
        assertTrue(actualProductRequest.getAvailable());
        assertEquals(1, actualProductRequest.getTimeToStock());
        assertEquals("Summary", actualProductRequest.getSummary());
        assertEquals(10.0f, actualProductRequest.getSalePrice());
        assertEquals(1, actualProductRequest.getRating());
        assertEquals(10.0f, actualProductRequest.getPrice());
        assertTrue(actualProductRequest.getOnSale());
        assertEquals("Name", actualProductRequest.getName());
        assertTrue(actualProductRequest.getInStock());
        assertEquals("Image", actualProductRequest.getImage());
        assertEquals("The characteristics of someone or something", actualProductRequest.getDescription());
        assertEquals("Code", actualProductRequest.getCode());
    }
}

