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
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * Product entity
 * @author Kevin A. Lee
 */
@Entity
@Table(name = "products")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "products";

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

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

    @Min(value = 0, message = "{product.price.invalidValue}")
    private float price;

    @NotNull
    @Column(name = "on_sale")
    private Boolean onSale;

    @Min(value = 0, message = "{product.price.invalidValue}")
    @Column(name = "sale_price")
    private float salePrice;

    @NotNull
    @Column(name = "in_stock")
    private Boolean inStock;

    @Min(value = 1, message = "{product.time_to_stock.invalidValue}")
    @Max(value = 365, message = "{product.time_to_stock.invalidValue}")
    @Column(name = "time_to_stock")
    private int timeToStock;

    @Min(value = 1, message = "{product.rating.invalidValue}")
    @Max(value = 5, message = "{product.rating.invalidValue}")
    private int rating;

    @NotNull
    private Boolean available;

    public Product() {
    }

    public Product(UUID id, String code, String name, String summary, String description, String image,
                   float price, boolean onSale, float salePrice, boolean inStock, int timeToStock, int rating,
                   boolean available) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.summary = summary;
        this.description = description;
        this.image = image;
        this.price = price;
        this.onSale = onSale;
        this.salePrice = salePrice;
        this.inStock = inStock;
        this.timeToStock = timeToStock;
        this.rating = rating;
        this.available = available;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Boolean getOnSale() {
        return onSale;
    }

    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }

    public float getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(float salePrice) {
        this.salePrice = salePrice;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public int getTimeToStock() {
        return timeToStock;
    }

    public void setTimeToStock(int timeToStock) {
        this.timeToStock = timeToStock;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Product(" + id + " : " + name + " : SRP : " + price + ")";
    }

}
