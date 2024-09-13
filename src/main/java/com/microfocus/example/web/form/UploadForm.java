/*
        Insecure Web App (IWA)

        Copyright (C) 2021-2024 Micro Focus or one of its affiliates

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
import java.util.UUID;

import org.springframework.beans.BeanUtils;

/**
 * Form backing entity/DTO for uploading a file
 *
 * @author kadraman
 */
public class UploadForm {


    private UUID id;
    private String username;
    private String file;


    public UploadForm() {
    }

    public UploadForm(User user) {
        BeanUtils.copyProperties(user, this);
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "UploadForm [id=" + id + ", username=" + username + ", file=" + file + "]";
    }

}
