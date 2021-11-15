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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.microfocus.example.entity.Order;
import com.microfocus.example.entity.User;

import java.util.Date;
import java.util.UUID;

/**
 * Order Response DTO
 *
 * @author Kevin A. Lee
 */
public class OrderResponse {

    private UUID id;
    private UserResponse user;
    private String orderNum;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date orderDate;
    private String cart;
    private float amount;
    private Boolean shipped;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date shippedDate;

    public OrderResponse() {
    }

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.user = new UserResponse(order.getUser());
        this.orderNum = order.getOrderNum();
        this.orderDate = order.getOrderDate();
        this.cart = order.getCart();
        this.amount = order.getAmount();
        this.shipped = order.getShipped();
        this.shippedDate = order.getShippedDate();
    }

    public UUID getId() {
        return id;
    }

    public UserResponse getUser() {
        return user;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public String getCart() {
        return cart;
    }

    public float getAmount() {
        return amount;
    }

    public Boolean getShipped() {
        return shipped;
    }

    public Date getShippedDate() {
        return shippedDate;
    }

    @Override
    public String toString() {
        return "OrderResponse(" + orderNum + " : amount : " + amount + ")";
    }

}
