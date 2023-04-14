package com.microfocus.example.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class ProductTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Product#Product()}
     *   <li>{@link Product#setAvailable(Boolean)}
     *   <li>{@link Product#setCode(String)}
     *   <li>{@link Product#setDescription(String)}
     *   <li>{@link Product#setId(UUID)}
     *   <li>{@link Product#setImage(String)}
     *   <li>{@link Product#setInStock(Boolean)}
     *   <li>{@link Product#setName(String)}
     *   <li>{@link Product#setOnSale(Boolean)}
     *   <li>{@link Product#setPrice(float)}
     *   <li>{@link Product#setRating(int)}
     *   <li>{@link Product#setSalePrice(float)}
     *   <li>{@link Product#setSummary(String)}
     *   <li>{@link Product#setTimeToStock(int)}
     *   <li>{@link Product#toString()}
     *   <li>{@link Product#getAvailable()}
     *   <li>{@link Product#getCode()}
     *   <li>{@link Product#getDescription()}
     *   <li>{@link Product#getId()}
     *   <li>{@link Product#getImage()}
     *   <li>{@link Product#getInStock()}
     *   <li>{@link Product#getName()}
     *   <li>{@link Product#getOnSale()}
     *   <li>{@link Product#getPrice()}
     *   <li>{@link Product#getRating()}
     *   <li>{@link Product#getSalePrice()}
     *   <li>{@link Product#getSummary()}
     *   <li>{@link Product#getTimeToStock()}
     * </ul>
     */
    @Test
    void testConstructor() {
        Product actualProduct = new Product();
        actualProduct.setAvailable(true);
        actualProduct.setCode("Code");
        actualProduct.setDescription("The characteristics of someone or something");
        UUID randomUUIDResult = UUID.randomUUID();
        actualProduct.setId(randomUUIDResult);
        actualProduct.setImage("Image");
        actualProduct.setInStock(true);
        actualProduct.setName("Name");
        actualProduct.setOnSale(true);
        actualProduct.setPrice(10.0f);
        actualProduct.setRating(1);
        actualProduct.setSalePrice(10.0f);
        actualProduct.setSummary("Summary");
        actualProduct.setTimeToStock(1);
        actualProduct.toString();
        assertTrue(actualProduct.getAvailable());
        assertEquals("Code", actualProduct.getCode());
        assertEquals("The characteristics of someone or something", actualProduct.getDescription());
        assertSame(randomUUIDResult, actualProduct.getId());
        assertEquals("Image", actualProduct.getImage());
        assertTrue(actualProduct.getInStock());
        assertEquals("Name", actualProduct.getName());
        assertTrue(actualProduct.getOnSale());
        assertEquals(10.0f, actualProduct.getPrice());
        assertEquals(1, actualProduct.getRating());
        assertEquals(10.0f, actualProduct.getSalePrice());
        assertEquals("Summary", actualProduct.getSummary());
        assertEquals(1, actualProduct.getTimeToStock());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Product#Product(UUID, String, String, String, String, String, float, boolean, float, boolean, int, int, boolean)}
     *   <li>{@link Product#setAvailable(Boolean)}
     *   <li>{@link Product#setCode(String)}
     *   <li>{@link Product#setDescription(String)}
     *   <li>{@link Product#setId(UUID)}
     *   <li>{@link Product#setImage(String)}
     *   <li>{@link Product#setInStock(Boolean)}
     *   <li>{@link Product#setName(String)}
     *   <li>{@link Product#setOnSale(Boolean)}
     *   <li>{@link Product#setPrice(float)}
     *   <li>{@link Product#setRating(int)}
     *   <li>{@link Product#setSalePrice(float)}
     *   <li>{@link Product#setSummary(String)}
     *   <li>{@link Product#setTimeToStock(int)}
     *   <li>{@link Product#toString()}
     *   <li>{@link Product#getAvailable()}
     *   <li>{@link Product#getCode()}
     *   <li>{@link Product#getDescription()}
     *   <li>{@link Product#getId()}
     *   <li>{@link Product#getImage()}
     *   <li>{@link Product#getInStock()}
     *   <li>{@link Product#getName()}
     *   <li>{@link Product#getOnSale()}
     *   <li>{@link Product#getPrice()}
     *   <li>{@link Product#getRating()}
     *   <li>{@link Product#getSalePrice()}
     *   <li>{@link Product#getSummary()}
     *   <li>{@link Product#getTimeToStock()}
     * </ul>
     */
    @Test
    void testConstructor2() {
        Product actualProduct = new Product(UUID.randomUUID(), "Code", "Name", "Summary",
                "The characteristics of someone or something", "Image", 10.0f, true, 10.0f, true, 1, 1, true);
        actualProduct.setAvailable(true);
        actualProduct.setCode("Code");
        actualProduct.setDescription("The characteristics of someone or something");
        UUID randomUUIDResult = UUID.randomUUID();
        actualProduct.setId(randomUUIDResult);
        actualProduct.setImage("Image");
        actualProduct.setInStock(true);
        actualProduct.setName("Name");
        actualProduct.setOnSale(true);
        actualProduct.setPrice(10.0f);
        actualProduct.setRating(1);
        actualProduct.setSalePrice(10.0f);
        actualProduct.setSummary("Summary");
        actualProduct.setTimeToStock(1);
        actualProduct.toString();
        assertTrue(actualProduct.getAvailable());
        assertEquals("Code", actualProduct.getCode());
        assertEquals("The characteristics of someone or something", actualProduct.getDescription());
        assertSame(randomUUIDResult, actualProduct.getId());
        assertEquals("Image", actualProduct.getImage());
        assertTrue(actualProduct.getInStock());
        assertEquals("Name", actualProduct.getName());
        assertTrue(actualProduct.getOnSale());
        assertEquals(10.0f, actualProduct.getPrice());
        assertEquals(1, actualProduct.getRating());
        assertEquals(10.0f, actualProduct.getSalePrice());
        assertEquals("Summary", actualProduct.getSummary());
        assertEquals(1, actualProduct.getTimeToStock());
    }
}

