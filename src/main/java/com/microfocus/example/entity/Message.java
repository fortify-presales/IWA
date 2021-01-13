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

package com.microfocus.example.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * A user message.
 *
 * @author Kevin A. Lee
 */
@Entity
@Table(name = "messages")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    //private Integer id;

    @ManyToOne
    private User user;

    @NotEmpty(message = "{message.text.notEmpty}")
    @Size(min = 20, message = "{message.text.invalidLength}")
    private String text;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @DateTimeFormat(pattern = "MM-dd-yyyy")
    @Column(name = "sent_date")
    private Date sentDate;

    @DateTimeFormat(pattern = "MM-dd-yyyy")
    @Column(name = "read_date")
    private Date readDate;

    @NotNull
    private Boolean read;

    public Message() {
        this.read = false;
        Calendar calendar = Calendar.getInstance();
        this.sentDate = calendar.getTime();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    public Date getSentDate() {
        return this.sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    public Date getReadDate() {
        return this.readDate;
    }

    public void setReadDate(Date readDate) {
        this.readDate = readDate;
    }

    public Boolean getRead() {
        return this.read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    @Override
    public String toString() {
        return "Message(" + id + " to: " + user.getUsername() + " on: " + sentDate.toString() + ":" + text + "!)";
    }

}
