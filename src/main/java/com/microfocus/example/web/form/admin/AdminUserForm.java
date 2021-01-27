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

import com.microfocus.example.entity.User;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.util.UUID;

/**
 * Form backing entity/DTO for updating user profile
 *
 * @author Kevin A. Lee
 */
public class AdminUserForm {

    private UUID id;

    @NotEmpty(message = "{user.username.notEmpty}")
    @Size(min = 2, max = 10, message = "{user.username.invalidLength}")
    private String username;

    @NotEmpty(message = "{user.firstName.notEmpty}")
    @Size(min = 2, max = 40, message = "{user.firstName.invalidLength}")
    private String firstName;

    @NotEmpty(message = "{user.firstName.notEmpty}")
    @Size(min = 2, max = 40, message = "{user.firstName.invalidLength}")
    private String lastName;

    @NotEmpty(message = "{user.email.notEmpty}")
    @Email(message = "{user.email.invalidFormat")
    private String email;

    @NotEmpty(message = "{user.phone.notEmpty}")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "{user.phone.invalidFormat}")
    @Column(unique = true)
    private String phone;

    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;

    private Boolean enabled;

    public AdminUserForm() {
    }

    public AdminUserForm(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.city = user.getCity();
        this.state = user.getState();
        this.zip = user.getZip();
        this.country = user.getCountry();
        this.enabled = user.getEnabled();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "AdminUserForm{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email =" + email +
                '}';
    }
}
