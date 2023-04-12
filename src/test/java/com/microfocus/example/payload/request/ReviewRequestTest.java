package com.microfocus.example.payload.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.microfocus.example.entity.Product;
import com.microfocus.example.entity.Review;
import com.microfocus.example.entity.User;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class ReviewRequestTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link ReviewRequest#ReviewRequest()}
     *   <li>{@link ReviewRequest#setComment(String)}
     *   <li>{@link ReviewRequest#setId(UUID)}
     *   <li>{@link ReviewRequest#setProductId(UUID)}
     *   <li>{@link ReviewRequest#setRating(int)}
     *   <li>{@link ReviewRequest#setReviewDate(Date)}
     *   <li>{@link ReviewRequest#setUserId(UUID)}
     *   <li>{@link ReviewRequest#toString()}
     *   <li>{@link ReviewRequest#getComment()}
     *   <li>{@link ReviewRequest#getId()}
     *   <li>{@link ReviewRequest#getProductId()}
     *   <li>{@link ReviewRequest#getRating()}
     *   <li>{@link ReviewRequest#getReviewDate()}
     *   <li>{@link ReviewRequest#getUserId()}
     * </ul>
     */
    @Test
    void testConstructor() {
        ReviewRequest actualReviewRequest = new ReviewRequest();
        actualReviewRequest.setComment("Comment");
        UUID randomUUIDResult = UUID.randomUUID();
        actualReviewRequest.setId(randomUUIDResult);
        UUID randomUUIDResult1 = UUID.randomUUID();
        actualReviewRequest.setProductId(randomUUIDResult1);
        actualReviewRequest.setRating(1);
        Date fromResult = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        actualReviewRequest.setReviewDate(fromResult);
        UUID randomUUIDResult2 = UUID.randomUUID();
        actualReviewRequest.setUserId(randomUUIDResult2);
        actualReviewRequest.toString();
        assertEquals("Comment", actualReviewRequest.getComment());
        assertSame(randomUUIDResult, actualReviewRequest.getId());
        assertSame(randomUUIDResult1, actualReviewRequest.getProductId());
        assertEquals(1, actualReviewRequest.getRating());
        assertSame(fromResult, actualReviewRequest.getReviewDate());
        assertSame(randomUUIDResult2, actualReviewRequest.getUserId());
    }

    /**
     * Method under test: {@link ReviewRequest#ReviewRequest(Review)}
     */
    @Test
    void testConstructor2() {
        Product product = new Product();
        product.setAvailable(true);
        product.setCode("Code");
        product.setDescription("The characteristics of someone or something");
        UUID randomUUIDResult = UUID.randomUUID();
        product.setId(randomUUIDResult);
        product.setImage("Image");
        product.setInStock(true);
        product.setName("Name");
        product.setOnSale(true);
        product.setPrice(10.0f);
        product.setRating(1);
        product.setSalePrice(10.0f);
        product.setSummary("Summary");
        product.setTimeToStock(1);

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
        UUID randomUUIDResult1 = UUID.randomUUID();
        user.setId(randomUUIDResult1);
        user.setLastName("Doe");
        user.setMfa(true);
        user.setPassword("iloveyou");
        user.setPhone("6625550144");
        user.setState("MD");
        user.setUsername("janedoe");
        user.setVerifyCode("Verify Code");
        user.setZip("21654");

        Review review = new Review();
        review.setComment("Comment");
        review.setId(UUID.randomUUID());
        review.setProduct(product);
        review.setRating(1);
        Date fromResult = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        review.setReviewDate(fromResult);
        review.setUser(user);
        review.setVisible(true);
        ReviewRequest actualReviewRequest = new ReviewRequest(review);
        assertEquals("Comment", actualReviewRequest.getComment());
        assertSame(randomUUIDResult1, actualReviewRequest.getUserId());
        assertSame(fromResult, actualReviewRequest.getReviewDate());
        assertEquals(1, actualReviewRequest.getRating());
        assertSame(randomUUIDResult, actualReviewRequest.getProductId());
    }
}

