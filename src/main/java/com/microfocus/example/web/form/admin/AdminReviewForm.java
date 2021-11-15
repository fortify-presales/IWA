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

package com.microfocus.example.web.form.admin;

import com.microfocus.example.entity.Product;
import com.microfocus.example.entity.Review;
import com.microfocus.example.entity.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.UUID;

/**
 * Form backing entity/DTO for updating review
 *
 * @author Kevin A. Lee
 */
public class AdminReviewForm {

    private UUID id;

    private Product product;

    private User user;

    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private Date reviewDate;

    private String comment;

    @Min(value = 1, message = "{product.rating.invalidValue}")
    @Max(value = 5, message = "{product.rating.invalidValue}")
    private int rating;

    private Boolean visible;

    public AdminReviewForm() {
    }

    public AdminReviewForm(Review review) {
        this.id = review.getId();
        this.product = review.getProduct();
        this.user = review.getUser();
        this.reviewDate = review.getReviewDate();
        this.comment = review.getComment();
        this.rating = review.getRating();
        this.visible = review.getVisible();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "AdminReviewForm(" + id + " of: " + product.getName() + " by: " + user.getUsername() + " on : " + reviewDate + ")";
    }

}
