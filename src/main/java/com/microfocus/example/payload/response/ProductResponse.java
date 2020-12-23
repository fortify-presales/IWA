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

package com.microfocus.example.payload.response;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.microfocus.example.entity.Product;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * Product Response DTO
 *
 * @author Kevin A. Lee
 */
public class ProductResponse {

    private Integer id;
    private String code;
    private String name;
    private String summary;
    private String description;
    private String image;
    private float trade_price;
    private float retail_price;
    private int delivery_time;
    private int average_rating;
    private Boolean available;

    public ProductResponse() {
    }

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.code = product.getCode();
        this.name = product.getName();
        this.summary = product.getSummary();
        this.description = product.getDescription();
        this.image = product.getImage();
        this.trade_price = product.getTradePrice();
        this.retail_price = product.getRetailPrice();
        this.delivery_time = product.getDeliveryTime();
        this.average_rating = product.getAverageRating();
        this.available = product.getAvailable();
    }

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public float getTradePrice() {
        return trade_price;
    }

    public float getRetailPrice() {
        return retail_price;
    }

    public int getDeliveryTime() {
        return delivery_time;
    }

    public int getAverageRating() {
        return average_rating;
    }

    public Boolean getAvailable() {
        return available;
    }

    @Override
    public String toString() {
        return "ProductResponse(" + name + " : SRP : " + retail_price + ")";
    }

}
