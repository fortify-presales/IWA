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

package com.microfocus.example.web.form.admin;

import com.microfocus.example.entity.Product;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Form backing entity/DTO for updating product
 *
 * @author Kevin A. Lee
 */
public class AdminNewProductForm {

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

    private Boolean onSale;

    @Min(value = 0, message = "{product.price.invalidValue}")
    private float salePrice;

    private Boolean inStock;

    @Min(value = 0, message = "{product.timeToStock.invalidValue}")
    @Max(value = 365, message = "{product.timeToStock.invalidValue}")
    private int timeToStock;

    @Min(value = 1, message = "{product.rating.invalidValue}")
    @Max(value = 5, message = "{product.rating.invalidValue}")
    private int rating;

    private Boolean available;

    public AdminNewProductForm() {
    }

    public AdminNewProductForm(Product product) {
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
        return "ProductForm(" + id + " : " + name + " : SRP : " + price + ")";
    }

}
