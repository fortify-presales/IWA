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

import com.microfocus.example.entity.Product;

import java.util.UUID;

/**
 * Product Response DTO
 *
 * @author Kevin A. Lee
 */
public class ProductResponse {

    private UUID id;
    private String code;
    private String name;
    private String summary;
    private String description;
    private String image;
    private float price;
    private Boolean onSale;
    private float salePrice;
    private Boolean inStock;
    private int timeToStock;
    private int rating;
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
        this.price = product.getPrice();
        this.onSale = product.getOnSale();
        this.salePrice = product.getSalePrice();
        this.inStock = product.getInStock();
        this.timeToStock = product.getTimeToStock();
        this.rating = product.getRating();
        this.available = product.getAvailable();
    }

    public UUID getId() {
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

    public float getPrice() {
        return price;
    }

    public Boolean getOnSale() {
        return onSale;
    }

    public float getSalePrice() {
        return salePrice;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public int getTimeToStock() {
        return timeToStock;
    }

    public int getRating() {
        return rating;
    }

    public Boolean getAvailable() {
        return available;
    }

    @Override
    public String toString() {
        return "ProductResponse(" + name + " : SRP : " + price + ")";
    }

}
