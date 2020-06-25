/*
        Java Web App

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

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Product entiy
 * @author Kevin A. Lee
 */
@Entity
@Table(name = "product")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message= "Product code must not be blank")
    private String code;

    @NotEmpty(message= "Product name must not be blank")
    private String name;

    @NotEmpty(message= "Product summary must not be blank")
    private String summary;

    @NotEmpty(message= "Product description must not be blank")
    private String description;

    @NotEmpty(message= "Product trace price must not be blank")
    private float trade_price;

    @NotEmpty(message= "Product retail price must not be blank")
    private float retail_price;

    @NotEmpty(message= "Product delivery time must not be blank")
    private int delivery_time;

    @NotNull
    private int average_rating;

    @NotNull
    private Boolean available;

    public Product() {
    }

    public Product(Integer id, String code, String name, String summary, String description, float tradePrice,
                   float retailPrice, int deliveryTime, int averageRating, boolean available) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.summary = summary;
        this.description = description;
        this.trade_price = tradePrice;
        this.retail_price = retailPrice;
        this.delivery_time = deliveryTime;
        this.average_rating = averageRating;
        this.available = available;
    }

    public long getId() {
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
