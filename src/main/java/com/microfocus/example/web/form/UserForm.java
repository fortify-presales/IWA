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

package com.microfocus.example.web.form;

import com.microfocus.example.entity.Authority;
import com.microfocus.example.entity.User;
import com.microfocus.example.utils.EncryptedPasswordUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class UserForm {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Min(1)
    private Integer id;

    @NotEmpty(message = "{username.notEmpty}")
    @Size(min=2, max=10, message = "{username.invalidLength}")
    private String username;

    @NotEmpty(message = "{password.notEmpty}")
    @Size(min=6, max=20, message = "{password.invalidLength}")
    private String password;

    private String confirmPassword;

    @NotEmpty(message = "{name.notEmpty}")
    @Size(min=6, max=40, message = "{name.invalidLength}")
    private String name;

    @NotEmpty(message = "{email.notEmpty}")
    @Email(message = "{email.invalidFormat")
    private String email;

    @NotEmpty(message = "{mobile.notEmpty")
    @Pattern(regexp="(^$|[0-9]{12})", message = "{mobile.invalidFormat}")
    private String mobile;

    public UserForm() {
    }

    public UserForm(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
        this.mobile = user.getMobile();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "UserForm{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email =" + email +
                '}';
    }
}
