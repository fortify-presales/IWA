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

package com.microfocus.example.repository.impl;

import com.microfocus.example.entity.User;
import com.microfocus.example.repository.UserRepository;
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
 * Implementation of basic User Repository
 *
 * @author Kevin A. Lee
 */
@Repository("ssaUserRepository")
@Transactional
public class UserRepositoryImpl implements UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @PersistenceContext
    EntityManager entityManager;

    private UserRepository userRepository;

    @Override
    public <S extends User> S save(S entity) {
        return userRepository.save(entity);
    }

    @Override
    public <S extends User> Iterable<S> saveAll(Iterable<S> iterable) {
        return userRepository.saveAll(iterable);
    }

    @Override
    public Optional<User> findById(Integer aInteger) {
        return userRepository.findById(aInteger);
    }

    @Override
    public boolean existsById(Integer aInteger) {
        return userRepository.existsById(aInteger);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Iterable<User> findAllById(Iterable<Integer> iterable) {
        return userRepository.findAllById(iterable);
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public void deleteById(Integer aInteger) {
        userRepository.deleteById(aInteger);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public void deleteAll(Iterable<? extends User> iterable) {
        userRepository.deleteAll(iterable);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        Query query = (Query) entityManager.createNativeQuery("SELECT * FROM user WHERE username = ?", User.class);
        query.setParameter(1, username);
        List <User> results = (List <User>) query.getResultList();
        Optional<User> optionalUser = Optional.empty();
        if (!query.getResultList().isEmpty()) {
            optionalUser = Optional.of(results.get(0));
        }
        return optionalUser;
    }
}
