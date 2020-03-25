/*
        Simple Secure App

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

import com.microfocus.example.entity.User;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of Custom User Repository
 *
 * @author Kevin A. Lee
 */
@Repository
@Transactional
public class CustomUserRepository implements ICustomUserRepository {

    private static final Logger log = LoggerFactory.getLogger(CustomUserRepository.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<User> findUserByUsername(String username) {
        Query query = (Query) entityManager.createNativeQuery("SELECT * FROM user WHERE username = ?", User.class);
        query.setParameter(1, username);
        List<User> results = (List<User>) query.getResultList();
        Optional<User> optionalUser = Optional.empty();
        if (!query.getResultList().isEmpty()) {
            optionalUser = Optional.of(results.get(0));
        }
        return optionalUser;
    }
}
