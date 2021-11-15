package com.microfocus.example.repository;

import com.microfocus.example.BaseIntegrationTest;
import com.microfocus.example.DataSeeder;
import com.microfocus.example.entity.Review;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReviewRepositoryTest extends BaseIntegrationTest {

    @Autowired
    ReviewRepository ReviewRepository;

    @Test
    public void a_ReviewRepository_existsById() {
        if (!ReviewRepository.existsById(DataSeeder.TEST_REVIEW1_ID)) fail("Review "+ DataSeeder.TEST_REVIEW1_ID + " does not exist");
    }

    @Test
    public void b_ReviewRepository_findReviewById() {
        Optional<Review> m = ReviewRepository.findById(DataSeeder.TEST_REVIEW1_ID);
        if (m.isPresent())
            assertThat(m.get().getComment()).isEqualTo(DataSeeder.TEST_REVIEW1_COMMENT);
        else
            fail("Test Review 1 not found");
    }

    @Test
    public void c_ReviewRepository_findReviewByProductId() {
        List<Review> Reviews = ReviewRepository.findProductReviews(DataSeeder.TEST_REVIEW1_PRODUCTID);
        assertThat(Reviews.size()).isEqualTo(1);
    }

    @Test
    public void c_ReviewRepository_findReviewByUserId() {
        List<Review> Reviews = ReviewRepository.findByUserId(DataSeeder.TEST_REVIEW1_USERID);
        assertThat(Reviews.size()).isEqualTo(1);
    }

}
