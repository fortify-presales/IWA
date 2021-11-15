/*
        Insecure Web App (IWA)

        Copyright (C) 2021 Micro Focus or one of its affiliates

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.microfocus.example.repository;

import com.microfocus.example.entity.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of Custom Product Review Repository
 * @author Kevin A. Lee
 */
@Transactional
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(ReviewRepositoryImpl.class);

    private final ReviewRepositoryBasic reviewRepositoryBasic;

    @PersistenceContext
    EntityManager entityManager;

    public ReviewRepositoryImpl(ReviewRepositoryBasic reviewRepositoryBasic) {
        this.reviewRepositoryBasic = reviewRepositoryBasic;
    }

    @SuppressWarnings("unchecked")
    public List<Review> findProductReviews(UUID productId) {
        Query query = entityManager.createQuery(
                "SELECT r FROM Review r WHERE r.product.id = ?1",
                Review.class);
        query.setParameter(1, productId);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Review> findByUserId(UUID userId) {
        Query query = entityManager.createQuery(
                "SELECT r FROM Review r WHERE r.user.id = ?1",
                Review.class);
        query.setParameter(1, userId);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Review> findReviews(int offset, int limit) {
        List<Review> result = new ArrayList<>();
        Query q = entityManager.createQuery(
                "SELECT r FROM Review r",
                Review.class);
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        result = (List<Review>)q.getResultList();
        return result;
    }

    @SuppressWarnings("unchecked")
    public long countByProductId(UUID productId) {
        Query query = entityManager.createQuery(
                "SELECT count(r) FROM Review r WHERE r.product.id = ?1",
                Long.class);
        query.setParameter(1, productId);
        return (long)(query.getSingleResult());
    }

    @SuppressWarnings("unchecked")
    public long countByUserId(UUID userId) {
        Query query = entityManager.createQuery(
                "SELECT count(r) FROM Review r WHERE r.user.id = ?1",
                Long.class);
        query.setParameter(1, userId);
        return (long)(query.getSingleResult());
    }

    @SuppressWarnings("unchecked")
    public List<Review> findReviewsByKeywords(String keywords, int offset, int limit) {
        List<Review> result = new ArrayList<>();
        Query q = entityManager.createQuery(
                "SELECT r FROM Review r WHERE lower(r.comment) LIKE lower(?1)",
                Review.class);
        q.setParameter(1, "%"+keywords+"%");
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        result = (List<Review>)q.getResultList();
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Review> findProductReviewsByKeywords(UUID productId, String keywords, int offset, int limit) {
        List<Review> result = new ArrayList<>();
        Query q = entityManager.createQuery(
                "SELECT r FROM Review r WHERE r.product.id = '?1' AND lower(r.comment) LIKE lower(?2)",
                Review.class);
        q.setParameter(1, productId.toString());
        q.setParameter(2, "%"+keywords+"%");
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        result = (List<Review>)q.getResultList();
        return result;
    }

    public Review addProductReview(UUID productId, UUID userId, String comment, int rating) {
        UUID reviewId = UUID.randomUUID();
        entityManager.createNativeQuery(
                "INSERT INTO reviews (id, product_id, user_id, comment, rating) " +
                        "VALUES (" +
                            reviewId + "," +
                            productId + "," +
                            userId + "," +
                            "'" + comment + "'," +
                            rating +
                        ")")
                .executeUpdate();
        return entityManager.find(Review.class, reviewId);
    }

    /*public Review save(ReviewRequest Review) {
        Review o =  new Review();
        o.setReviewNum(Review.getReviewNum());
        o.setUser(Review.getUserId());
        o.setReviewDate(Review.getReviewDate());
        o.setAmount(Review.getAmount());
        o.setCart(Review.getCart());
        o.setShipped(Review.getShipped());
        o.setShippedDate(Review.getShippedDate());
        return ReviewRepositoryBasic.save(o);
    }*/

}
