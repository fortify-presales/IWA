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

import com.microfocus.example.entity.Product;
import com.microfocus.example.entity.User;
import com.microfocus.example.exception.UserLockedOutException;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Optional<User> findUserByUsername(String username) throws UserLockedOutException {
        List<User> result = new ArrayList<>();
        Query q = entityManager.createQuery(
		        "SELECT u FROM User u WHERE u.username = ?1", User.class);
        q.setParameter(1, username);
        result = (List<User>) q.getResultList();
        Optional<User> optionalUser = Optional.empty();
        if (!result.isEmpty()) {
            optionalUser = Optional.of(result.get(0));
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
