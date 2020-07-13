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

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.microfocus.example.web.validation.ValidPassword;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Custom User entity
 * @author Kevin A. Lee
 */
@Entity
@Table(name = "user")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "{user.username.notEmpty}")
    @Size(min = 2, max = 10, message = "{user.username.invalidLength}")
    @Column(nullable = false, unique = true)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Transient
    private String confirmPassword;

    @NotEmpty(message = "{user.name.notEmpty}")
    @Size(min = 6, max = 40, message = "{user.name.invalidLength}")
    private String name;

    @NotEmpty(message = "{user.email.notEmpty}")
    @Email(message = "{user.email.invalidFormat")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "{user.mobile.notEmpty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "{user.mobile.invalidFormat}")
    @Column(unique = true)
    private String mobile;

    @JsonProperty(access =  JsonProperty.Access.WRITE_ONLY)
    @Column(name = "date_created")
    private Date dateCreated;

    @JsonProperty("enabled")
    private boolean enabled;

    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "authority_id") })
    private Set<Authority> authorities = new HashSet<>();

    public User() {
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

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public static User fromUserDetails(UserDetails user) {
        Set<Authority> authorities = new HashSet<>(Collections.emptySet());
        for (GrantedAuthority a : user.getAuthorities()) {
            authorities.add(new Authority(AuthorityType.valueOf(a.getAuthority())));
        }
        User utmp = new User();
        utmp.setUsername(user.getUsername());
        utmp.setPassword(user.getPassword());
        utmp.setAuthorities(authorities);
        utmp.setEnabled(user.isEnabled());
        return utmp;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
