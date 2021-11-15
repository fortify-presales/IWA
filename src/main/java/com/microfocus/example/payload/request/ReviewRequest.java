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

package com.microfocus.example.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microfocus.example.entity.Review;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

/**
 * Review Request DTO
 *
 * @author Kevin A. Lee
 */
public class ReviewRequest {

    @JsonIgnore
    private UUID id;

    private UUID productId;

    private UUID userId;

    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private Date reviewDate;

    private String comment;

    private int rating;

    public ReviewRequest() {
    }

    public ReviewRequest(Review review) {
        this.productId = review.getProduct().getId();
        this.userId = review.getUser().getId();
        this.reviewDate = review.getReviewDate();
        this.comment = review.getComment();
        this.rating = review.getRating();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return this.productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "ReviewRequest(" + id + " of: " + productId + " by: " + userId + " on : " + reviewDate + ")";
    }
}
