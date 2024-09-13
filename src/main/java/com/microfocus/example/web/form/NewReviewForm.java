/*
        Insecure Web App (IWA)

        Copyright (C) 2021-2024 Micro Focus or one of its affiliates

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

import com.microfocus.example.entity.Review;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.UUID;

/**
 * Form backing entity/DTO for new review
 *
 * @author kadraman
 */
public class NewReviewForm {

    private UUID id;

    private UUID productId;
    private String productName;

    private UUID userId;

    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private Date reviewDate;

    private String comment;

    @Min(value = 1, message = "{product.rating.invalidValue}")
    @Max(value = 5, message = "{product.rating.invalidValue}")
    private int rating;

    private Boolean visible;

    public NewReviewForm() {

    }

    public NewReviewForm(UUID u, UUID pid, String pname) {
        this.userId = u;
        this.productId = pid;
        this.productName = pname;
        this.reviewDate = new Date();
        this.comment = "";
        this.rating = 1;
        this.visible = true;
    }

    public NewReviewForm(Review review) {
        BeanUtils.copyProperties(review, this);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public UUID getUserId() {
        return userId;
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

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "NewReviewForm [id=" + id + ", productId=" + productId + ", productName=" + productName + ", userId="
                + userId + ", reviewDate=" + reviewDate + ", comment=" + comment + ", rating=" + rating + ", visible="
                + visible + "]";
    }

}
