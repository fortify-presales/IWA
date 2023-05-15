/*
        Insecure Web App (IWA)

        Copyright (C) 2020 Micro Focus or one of its affiliates

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

package com.microfocus.example.web.form;

import com.microfocus.example.entity.Product;
import com.microfocus.example.entity.Review;
import com.microfocus.example.entity.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

/**
 * Form backing entity/DTO for updating product review
 *
 * @author Kevin A. Lee
 */
public class ReviewForm {

    private UUID id;

    private User user;

    private Product product;

    private String comment;

    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private Date reviewDate;

    private int rating;

    public ReviewForm(Review review) {
        this.id = review.getId();
        this.user = review.getUser();
        this.product = review.getProduct();
        this.comment = review.getComment();
        this.reviewDate = review.getReviewDate();
        this.rating = review.getRating();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "ReviewForm{" +
                "id=" + id +
                ", user=" + user.getUsername() +
                ", product=" + product.getName() +
                ", comment='" + comment + '\'' +
                ", reviewData=" + reviewDate +
                ", rating=" + rating +
                '}';
    }
}
