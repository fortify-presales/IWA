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

import com.microfocus.example.entity.User;
import com.microfocus.example.web.validation.ValidPassword;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Form backing entity/DTO for changing password
 * @author Kevin A. Lee
 */
public class PasswordForm {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @NotEmpty
    private String username;

    //@NotEmpty(message = "{password.notEmpty}")
    //@Size(min=6, max=20, message = "{password.invalidLength}")
    @ValidPassword
    private String password;

    //@NotEmpty(message = "{confirm.notEmpty}")
    //@Size(min=6, max=20, message = "{confirm.invalidLength}")
    @ValidPassword
    private String confirmPassword;

    public PasswordForm() {
    }

    public PasswordForm(User user) {
        this.username = user.getUsername();
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

     @Override
    public String toString() {
        return "PasswordForm{" +
                ", username='" + username + '\'' +
                '}';
    }
}
