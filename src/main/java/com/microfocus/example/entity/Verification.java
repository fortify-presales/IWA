/*
        Insecure Web App (IWA)
        Copyright (C) 2020-2022 Micro Focus or one of its affiliates
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * MFA Verification entity
 * @author Kevin A. Lee
 */
@Entity
@Table(name = "verifications")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Verification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "request_id", nullable = false)
    private String requestId;

    @Column(name = "expiration_date")
    private Date expirationDate;

    public Verification() {
    }

    public Verification(String phone, String requestId, Date expirationDate) {
        this.phone = phone;
        this.requestId = requestId;
        this.expirationDate = expirationDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "Verification{" +
                "phone='" + phone + '\'' +
                ", requestId='" + requestId + '\'' +
                ", expirationDate=" + expirationDate +
                '}';
    }
}