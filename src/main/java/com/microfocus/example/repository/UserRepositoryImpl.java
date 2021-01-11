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

package com.microfocus.example.repository;

import com.microfocus.example.entity.Authority;
import com.microfocus.example.entity.AuthorityType;
import com.microfocus.example.entity.Product;
import com.microfocus.example.entity.User;
import com.microfocus.example.exception.UserLockedOutException;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Implementation of Custom User Repository
 * @author Kevin A. Lee
 */
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(UserRepositoryImpl.class);

    private final UserRepositoryBasic userRepositoryBasic;

    @PersistenceContext
    EntityManager entityManager;

    public UserRepositoryImpl(UserRepositoryBasic userRepositoryBasic) {
        this.userRepositoryBasic = userRepositoryBasic;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<User> findUserByUsername(String username) throws UserLockedOutException, UsernameNotFoundException {
        log.debug("UserRepositoryImpl::findUserByUsername");
        List<User> users = new ArrayList<>();

// INSECURE EXAMPLE: SQL Injection

        Session session = entityManager.unwrap(Session.class);
        Integer authorityCount = session.doReturningWork(new ReturningWork<Integer>() {

            @Override
            public Integer execute(Connection con) throws SQLException {
                Integer authorityCount = 0;
                try {
                    Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet results = stmt.executeQuery(
                            "SELECT u.id, u.username, u.password, u.name, u.email, u.mobile, u.enabled, a.name as authority " +
                                    "FROM User u, Authority a INNER JOIN User_Authority ua on a.id = ua.authority_id " +
                                    "WHERE u.id = ua.user_id AND u.username LIKE '%" + username + "%'");
                    if (results.next()) {
                        log.debug("Found matching user in database for: " + username);
                        results.beforeFirst();
                        User utmp = null;
                        Set<Authority> authorities = new HashSet<>();
                        while (results.next()) {
                            if (authorityCount == 0) {
                                utmp = new User(results.getInt("id"),
                                        results.getString("username"),
                                        results.getString("password"),
                                        results.getString("name"),
                                        results.getString("email"),
                                        results.getString("mobile"),
                                        results.getBoolean("enabled")
                                );
                                log.debug("Adding authority " + results.getString("authority") + " for user");
                                authorities.add(new Authority(AuthorityType.valueOf(results.getString("authority"))));
                                authorityCount++;
                            } else {
                                log.debug("Adding authority " + results.getString("authority") + " for user");
                                authorities.add(new Authority(AuthorityType.valueOf(results.getString("authority"))));
                            }
                        }
                        if (!authorities.isEmpty()) {
                            utmp.setAuthorities(authorities);
                        }
                        users.add(utmp);
                    } else {
                        log.debug("No matching users found");
                    }
                } catch (SQLException ex) {
                    log.error(ex.getLocalizedMessage());
                }
                return authorityCount;
            }
        });

        Optional<User> optionalUser = Optional.empty();
        if (!users.isEmpty()) {
            optionalUser = Optional.of(users.get(0));
        } else {
            log.debug("Unable to find username: " + username);
        }
        return optionalUser;
    }

    @SuppressWarnings("unchecked")
    public List<User> findUsersByUsername(String username) {
        List<User> result = new ArrayList<>();
        Query q = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username LIKE :username",
                User.class);
        q.setParameter("username", username);
        result = (List<User>)q.getResultList();
        return result;
    }

    @SuppressWarnings("unchecked")
	public List<User> findUsersByEnabledAndUsername(boolean enabled, String username) {
        List<User> result = new ArrayList<>();
        Query q = entityManager.createQuery(
        "SELECT u FROM User u WHERE u.enabled = :enabled AND u.username LIKE :username",
                User.class);
        q.setParameter("enabled", enabled);
        q.setParameter("username", username);
        result = (List<User>)q.getResultList();
        return result;
    }
}
