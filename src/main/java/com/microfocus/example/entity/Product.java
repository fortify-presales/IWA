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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.microfocus.example.utils.EncryptedPasswordUtils;
import org.hibernate.validator.constraints.Email;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    @NotEmpty(message= "Product name must not be blank")
    private String name;

    @NotEmpty(message= "Product description must not be blank")
    private String description;

    @NotEmpty(message= "Product trace price must not be blank")
    private float trade_price;

    @NotEmpty(message= "Product retail price must not be blank")
    private float retail_price;

    @NotEmpty(message= "Product delivery time must not be blank")
    private int delivery_time;

    @NotNull
    private Boolean available;

    public Product() {
    }

    public long getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setTradPrice(float trade_price) {
        this.trade_price = trade_price;
    }

    public float getRetailPrice() {
        return retail_price;
    }

    public void setRetaiPrice(float retail_price) {
        this.retail_price = retail_price;
    }

    public int getDeliveryTime() {
        return delivery_time;
    }

    public void setDeliveryTime(int delivery_time) {
        this.delivery_time = delivery_time;
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
