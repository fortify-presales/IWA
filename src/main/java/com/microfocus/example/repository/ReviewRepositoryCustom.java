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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface for Product Review Repository
 * @author Kevin A. Lee
 */
public interface ReviewRepositoryCustom {

    public List<Review> findProductReviews(UUID productId);

    public List<Review> findByUserId(UUID userId);

    List<Review> findReviews(int offset, int limit);

    public long countByUserId(UUID userId);

    public long countByProductId(UUID productId);

    List<Review> findReviewsByKeywords(String keywords, int offset, int limit);

    List<Review> findProductReviewsByKeywords(UUID productId, String keywords, int offset, int limit);

    public Review addProductReview(UUID productId, UUID userId, String comment, int rating);

}
