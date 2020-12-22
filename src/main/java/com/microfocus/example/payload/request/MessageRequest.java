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

import com.microfocus.example.entity.Message;
import com.microfocus.example.entity.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Message Request DTO
 *
 * @author Kevin A. Lee
 */
public class MessageRequest {

    //private Integer id;

    @Min(1)
    private Integer userId;

    @NotEmpty(message = "{message.text.notEmpty}")
    @Size(min = 40, message = "{message.text.invalidLength}")
    private String text;

    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private Date sentDate;

    private Boolean read;

    public MessageRequest() {
    }

    public MessageRequest(Message message) {
        //this.id = message.getId();
        this.userId = message.getUser().getId();
        this.text = message.getText();
        this.sentDate = message.getSentDate();
        this.read = false;
    }

/*
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
*/
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    @Override
    public String toString() {
        return "MessageRequest(user: " + userId + " message: " + text.substring(0,40) + ")";
    }

}
