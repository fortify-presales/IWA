package com.microfocus.example.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class ReviewTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Review#Review(UUID, User, Product, Date, String, int, boolean)}
     *   <li>{@link Review#setComment(String)}
     *   <li>{@link Review#setId(UUID)}
     *   <li>{@link Review#setProduct(Product)}
     *   <li>{@link Review#setRating(int)}
     *   <li>{@link Review#setReviewDate(Date)}
     *   <li>{@link Review#setUser(User)}
     *   <li>{@link Review#setVisible(Boolean)}
     *   <li>{@link Review#toString()}
     *   <li>{@link Review#getComment()}
     *   <li>{@link Review#getId()}
     *   <li>{@link Review#getProduct()}
     *   <li>{@link Review#getRating()}
     *   <li>{@link Review#getReviewDate()}
     *   <li>{@link Review#getUser()}
     *   <li>{@link Review#getVisible()}
     * </ul>
     */
    @Test
    void testConstructor() {
        UUID id = UUID.randomUUID();

        User user = new User();
        user.setAddress("42 Main St");
        user.setAuthorities(new HashSet<>());
        user.setCity("Oxford");
        user.setConfirmPassword("iloveyou");
        user.setCountry("GB");
        user.setDateCreated(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setEnabled(true);
        user.setFirstName("Jane");
        user.setId(UUID.randomUUID());
        user.setLastName("Doe");
        user.setMfa(true);
        user.setPassword("iloveyou");
        user.setPhone("6625550144");
        user.setState("MD");
        user.setUsername("janedoe");
        user.setVerifyCode("Verify Code");
        user.setZip("21654");

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
        Review actualReview = new Review(id, user, product,
                Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()), "Comment", 1, true);
        actualReview.setComment("Comment");
        UUID randomUUIDResult = UUID.randomUUID();
        actualReview.setId(randomUUIDResult);
        Product product1 = new Product();
        product1.setAvailable(true);
        product1.setCode("Code");
        product1.setDescription("The characteristics of someone or something");
        product1.setId(UUID.randomUUID());
        product1.setImage("Image");
        product1.setInStock(true);
        product1.setName("Name");
        product1.setOnSale(true);
        product1.setPrice(10.0f);
        product1.setRating(1);
        product1.setSalePrice(10.0f);
        product1.setSummary("Summary");
        product1.setTimeToStock(1);
        actualReview.setProduct(product1);
        actualReview.setRating(1);
        Date fromResult = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        actualReview.setReviewDate(fromResult);
        User user1 = new User();
        user1.setAddress("42 Main St");
        user1.setAuthorities(new HashSet<>());
        user1.setCity("Oxford");
        user1.setConfirmPassword("iloveyou");
        user1.setCountry("GB");
        user1.setDateCreated(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user1.setEmail("jane.doe@example.org");
        user1.setEnabled(true);
        user1.setFirstName("Jane");
        user1.setId(UUID.randomUUID());
        user1.setLastName("Doe");
        user1.setMfa(true);
        user1.setPassword("iloveyou");
        user1.setPhone("6625550144");
        user1.setState("MD");
        user1.setUsername("janedoe");
        user1.setVerifyCode("Verify Code");
        user1.setZip("21654");
        actualReview.setUser(user1);
        actualReview.setVisible(true);
        actualReview.toString();
        assertEquals("Comment", actualReview.getComment());
        assertSame(randomUUIDResult, actualReview.getId());
        assertSame(product1, actualReview.getProduct());
        assertEquals(1, actualReview.getRating());
        assertSame(fromResult, actualReview.getReviewDate());
        assertSame(user1, actualReview.getUser());
        assertTrue(actualReview.getVisible());
    }

    /**
     * Method under test: {@link Review#Review()}
     */
    @Test
    void testConstructor2() {
        assertFalse((new Review()).getVisible());
    }
}

