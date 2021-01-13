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

package com.microfocus.example.web.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microfocus.example.entity.Message;
import com.microfocus.example.entity.Product;
import com.microfocus.example.entity.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

/**
 * Form backing entity/DTO for updating message
 *
 * @author Kevin A. Lee
 */
public class MessageForm {

    private UUID id;

    private User user;

    @NotEmpty(message = "{message.text.notEmpty}")
    @Size(min = 40, message = "{message.text.invalidLength}")
    private String text;

    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private Date sentDate;

    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private Date readDate;

    private Boolean read;

    public MessageForm() {
    }

    public MessageForm(Message message) {
        this.id = message.getId();
        this.user = message.getUser();
        this.text = message.getText();
        this.sentDate = message.getSentDate();
        this.readDate = message.getReadDate();
        this.read = message.getRead();
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public Date getReadDate() {
        return readDate;
    }

    public void setReadDate(Date readDate) {
        this.readDate = readDate;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    @Override
    public String toString() {
        return "MessageForm(" + id + " : " + text.substring(0,40) + ")";
    }

}
