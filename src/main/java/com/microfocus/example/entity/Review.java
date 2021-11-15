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

package com.microfocus.example.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Product Review entity
 * @author Kevin A. Lee
 */
@Entity
@Table(name = "reviews")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private User user;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @DateTimeFormat(pattern = "MM-dd-yyyy")
    @Column(name = "review_date")
    private Date reviewDate;

    private String comment;

    @Min(value = 1, message = "{product.rating.invalidValue}")
    @Max(value = 5, message = "{product.rating.invalidValue}")
    private int rating;

    @NotNull
    private Boolean visible;

    public Review() {
        this.visible = false;
        Calendar calendar = Calendar.getInstance();
        this.reviewDate = calendar.getTime();
    }

    public Review(UUID id, User user, Product product, Date reviewDate, String comment, int rating, boolean visible) {
        this.id = id;
        this.product = product;
        this.user = user;
        this.reviewDate = reviewDate;
        this.comment = comment;
        this.rating = rating;
        this.visible = visible;
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
        return "Review(" + id + " of: " + product.getName() + " by: " + user.getUsername() + " on : " + reviewDate + ")";
    }

}
