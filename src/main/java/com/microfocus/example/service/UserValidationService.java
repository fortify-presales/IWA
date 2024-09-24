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

package com.microfocus.example.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.microfocus.example.entity.User;
import com.microfocus.example.web.form.RegisterUserForm;
import com.microfocus.example.web.form.SecurityForm;
import com.microfocus.example.web.form.UserForm;

/**
 * User Validation to validate related fields of user
 * 
 * @author kadraman
 */
@Service
public class UserValidationService {
    public String validateUser(User user) {
        String message = "";
        // TBD: validate phone number etc.
        return message;
    }
    public String validateUser(UserForm userForm) {
        User u = new User();
        BeanUtils.copyProperties(userForm, u);
        return this.validateUser(u);
    }
    public String validateUser(RegisterUserForm registerUserForm) {
        String message = "";
        // TBD: validate phone number etc.
        return message;
    }
    public String validateUser(SecurityForm securityForm) {
        String message = "";
        // TBD: validate MFA type with phone number, email etc.
        return message;
    }
}
