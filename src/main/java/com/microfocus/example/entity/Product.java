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
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * Product entiy
 * @author Kevin A. Lee
 */
@Entity
@Table(name = "product")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "{product.code.notEmpty}")
    @Size(min = 6, max = 40, message = "{product.code.invalidLength}")
    private String code;

    @NotEmpty(message = "{product.name.notEmpty}")
    @Size(min = 6, max = 40, message = "{product.name.invalidLength}")
    private String name;

    @NotEmpty(message = "{product.summary.notEmpty}")
    @Size(min = 10, message = "{product.summary.invalidLength}")
    private String summary;

    @NotEmpty(message = "{product.description.notEmpty}")
    @Size(min = 40, message = "{product.description.invalidLength}")
    private String description;

    private String image;

    @Min(value = 0, message = "{product.trade_price.invalidValue}")
    private float trade_price;

    @Min(value = 0, message = "{product.retail_price.invalidValue}")
    private float retail_price;

    @Min(value = 1, message = "{product.delivery_time.invalidValue}")
    @Max(value = 365, message = "{product.delivery_time.invalidValue}")
    private int delivery_time;

    @Min(value = 1, message = "{product.average_rating.invalidValue}")
    @Max(value = 5, message = "{product.average_rating.invalidValue}")
    private int average_rating;

    @NotNull
    private Boolean available;

    public Product() {
    }

    public Product(Integer id, String code, String name, String summary, String description, String image, float tradePrice,
                   float retailPrice, int deliveryTime, int averageRating, boolean available) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.summary = summary;
        this.description = description;
        this.image = image;
        this.trade_price = tradePrice;
        this.retail_price = retailPrice;
        this.delivery_time = deliveryTime;
        this.average_rating = averageRating;
        this.available = available;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getTradePrice() {
        return trade_price;
    }

    public void setTradePrice(float trade_price) {
        this.trade_price = trade_price;
    }

    public float getRetailPrice() {
        return retail_price;
    }

    public void setRetailPrice(float retail_price) {
        this.retail_price = retail_price;
    }

    public int getDeliveryTime() {
        return delivery_time;
    }

    public void setDeliveryTime(int delivery_time) {
        this.delivery_time = delivery_time;
    }

    public int getAverageRating() {
        return average_rating;
    }

    public void setAverageRating(int average_rating) {
        this.average_rating = average_rating;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Product(" + id + " : " + name + " : SRP : " + retail_price + ")";
    }

}
