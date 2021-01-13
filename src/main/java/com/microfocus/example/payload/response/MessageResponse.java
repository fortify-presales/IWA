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
import com.microfocus.example.entity.Message;
import java.util.Date;
import java.util.UUID;

/**
 * Message Response DTO
 *
 * @author Kevin A. Lee
 */
public class MessageResponse {

    private UUID id;
    private UserResponse user;
    private String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date sentDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date readDate;
    private Boolean read;

    public MessageResponse() {
    }

    public MessageResponse(Message message) {
        this.id = message.getId();
        this.user = new UserResponse(message.getUser());
        this.text = message.getText();
        this.sentDate = message.getSentDate();
        this.readDate = message.getReadDate();
        this.read = message.getRead();
    }

    public UUID getId() {
        return id;
    }

    public UserResponse getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public Date getReadDate() {
        return readDate;
    }

    public Boolean getRead() {
        return read;
    }

}
