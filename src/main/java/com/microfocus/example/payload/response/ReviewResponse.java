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

package com.microfocus.example.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.microfocus.example.entity.Order;
import com.microfocus.example.entity.Product;
import com.microfocus.example.entity.Review;
import com.microfocus.example.entity.User;

import java.util.Date;
import java.util.UUID;

/**
 * Order Response DTO
 *
 * @author Kevin A. Lee
 */
public class ReviewResponse {

    private UUID id;
    private ProductResponse product;
    private UserResponse user;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date reviewDate;
    private String comment;
    private int rating;

    public ReviewResponse() {
    }

    public ReviewResponse(Review review) {
        this.id = review.getId();
        this.product = new ProductResponse(review.getProduct());
        this.user = new UserResponse(review.getUser());
        this.reviewDate = review.getReviewDate();
        this.comment = review.getComment();
        this.rating = review.getRating();
    }

    public UUID getId() {
        return id;
    }

    public ProductResponse getProduct() {
        return this.product;
    }

    public UserResponse getUser() {
        return user;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }

    public String toString() {
        return "ReviewResponse(" + id + " of: " + product.getName() + " by: " + user.getUsername() + " on : " + reviewDate + ")";
    }
}
