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

package com.microfocus.example.payload.request;

import com.microfocus.example.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Register User Request DTO
 *
 * @author Kevin A. Lee
 */
public class RegisterUserRequest {

    @Bean("RegisterUserPasswordEncoder")
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @NotEmpty(message = "{user.username.notEmpty}")
    @Pattern(regexp = "(^[a-z|0-9]{4,10}$)", message = "{user.username.invalidFormat}")
    @Column(nullable = false, unique = true)
    private String username;

    @NotEmpty(message = "{user.password.notEmpty}")
    private String password;

    @NotEmpty(message = "{user.firstname.notEmpty}")
    @Size(min = 2, max = 40, message = "{user.firstname.invalidLength}")
    private String firstName;

    @NotEmpty(message = "{user.firstname.notEmpty}")
    @Size(min = 2, max = 40, message = "{user.firstname.invalidLength}")
    private String lastName;

    @NotEmpty(message = "{user.email.notEmpty}")
    @Email(message = "{user.email.invalidFormat")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "{user.phone.notEmpty}")
    @Pattern(regexp = "(^(?!0+$)[0-9]{7,12}$)", message = "{user.phone.invalidFormat}")
    @Column(unique = true)
    private String phone;

    public RegisterUserRequest() {
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

    @Override
    public String toString() {
        return "RegisterUserRequest{" +
                ", username='" + username + '\'' +
                ", email =" + email +
                '}';
    }
}
