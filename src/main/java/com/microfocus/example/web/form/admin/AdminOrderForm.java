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

import com.microfocus.example.entity.Order;
import com.microfocus.example.entity.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

/**
 * Form backing entity/DTO for updating order
 *
 * @author Kevin A. Lee
 */
public class AdminOrderForm {

    private UUID id;

    private User user;

    private String orderNum;

    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private Date orderDate;

    private float amount;

    private String cart;

    private Boolean shipped;

    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private Date shippedDate;

    public AdminOrderForm() {
    }

    public AdminOrderForm(Order order) {
        this.id = order.getId();
        this.user = order.getUser();
        this.orderNum = order.getOrderNum();
        this.orderDate = order.getOrderDate();
        this.amount = order.getAmount();
        this.cart = order.getCart();
        this.shipped = order.getShipped();
        this.shippedDate = order.getShippedDate();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getCart() {
        return this.cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public Boolean getShipped() {
        return shipped;
    }

    public void setShipped(Boolean shipped) {
        this.shipped = shipped;
    }

    public Date getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    @Override
    public String toString() {
        return "AdminOrderForm(" + id + " : " + orderNum + " for: " + user.getUsername() + " amount : " + amount + ")";
    }

}
