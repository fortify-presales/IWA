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

package com.microfocus.example.web.form;

import com.microfocus.example.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;

/**
 * Form backing entity/DTO for validating a new user
 *
 * @author Kevin A. Lee
 */
public class VerifyUserForm {

    @NotEmpty(message = "{user.email.notEmpty}")
    @Email(message = "{user.email.invalidFormat")
    private String email;

    @NotEmpty(message = "{user.verifyCode.notEmpty}")
    private String code;

    public VerifyUserForm() {
   }

    public VerifyUserForm(User user) {
        this.email = user.getEmail();
    }

    public VerifyUserForm(Optional<String> email, Optional<String> code) {
       this.email = (email.isPresent() ? email.get() : null);
       this.code = (code.isPresent() ? code.get() : null);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ValidateUserForm{" +
                "email=" + email +
                ", code=" + code +
                '}';
    }
}
