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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of Custom Product Review Repository
 * 
 * @author Kevin A. Lee
 */
@Transactional
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    public List<Review> findProductReviews(UUID productId) {
        TypedQuery<Review> query = entityManager.createQuery(
                "SELECT r FROM Review r WHERE r.product.id = ?1",
                Review.class);
        query.setParameter(1, productId);
        return query.getResultList();
    }

    public List<Review> findByUserId(UUID userId) {
        TypedQuery<Review> query = entityManager.createQuery(
                "SELECT r FROM Review r WHERE r.user.id = ?1",
                Review.class);
        query.setParameter(1, userId);
        return query.getResultList();
    }

    public List<Review> findReviews(int offset, int limit) {
        List<Review> result = new ArrayList<>();
        TypedQuery<Review> q = entityManager.createQuery(
                "SELECT r FROM Review r",
                Review.class);
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        result = (List<Review>) q.getResultList();
        return result;
    }

    public long countByProductId(UUID productId) {
        Query query = entityManager.createQuery(
                "SELECT count(r) FROM Review r WHERE r.product.id = ?1",
                Long.class);
        query.setParameter(1, productId);
        return (long) (query.getSingleResult());
    }

    public long countByUserId(UUID userId) {
        Query query = entityManager.createQuery(
                "SELECT count(r) FROM Review r WHERE r.user.id = ?1",
                Long.class);
        query.setParameter(1, userId);
        return (long) (query.getSingleResult());
    }

    public List<Review> findReviewsByKeywords(String keywords, int offset, int limit) {
        List<Review> result = new ArrayList<>();
        TypedQuery<Review> q = entityManager.createQuery(
                "SELECT r FROM Review r WHERE lower(r.comment) LIKE lower(?1)",
                Review.class);
        q.setParameter(1, "%" + keywords + "%");
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        result = (List<Review>) q.getResultList();
        return result;
    }

    public List<Review> findProductReviewsByKeywords(UUID productId, String keywords, int offset, int limit) {
        List<Review> result = new ArrayList<>();
        TypedQuery<Review> q = entityManager.createQuery(
                "SELECT r FROM Review r WHERE r.product.id = '?1' AND lower(r.comment) LIKE lower(?2)",
                Review.class);
        q.setParameter(1, productId.toString());
        q.setParameter(2, "%" + keywords + "%");
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        result = (List<Review>) q.getResultList();
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

}
