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

package com.microfocus.example.service;

import com.microfocus.example.entity.User;
import com.microfocus.example.exception.InvalidPasswordException;
import com.microfocus.example.exception.UserNotFoundException;
import com.microfocus.example.repository.IUserRepository;
import com.microfocus.example.utils.EncryptedPasswordUtils;
import com.microfocus.example.web.form.PasswordForm;
import com.microfocus.example.web.form.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * User Service to hide business logs / database persistance
 * @author Kevin A. Lee
 */
@Service
@Transactional
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private IUserRepository userRepository;

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public List<User> listAll() {
        return (List<User>) userRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User save(UserForm userForm) throws InvalidPasswordException, UserNotFoundException {
        Optional<User> optionalUser = userRepository.findUserByUsername(userForm.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!EncryptedPasswordUtils.matches(userForm.getPassword(), user.getPassword())) {
                throw new InvalidPasswordException("Password is incorrect");
            }
            user.setName(userForm.getName());
            user.setEmail(userForm.getEmail());
            user.setMobile(userForm.getMobile());
            return user;
        } else {
            throw new UserNotFoundException("Username not found: " + userForm.getUsername());
        }
    }

    public User updatePassword(Integer id, PasswordForm passwordForm) {
        if (passwordForm.getPassword().equals(passwordForm.getConfirmPassword())) {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setPassword(EncryptedPasswordUtils.encryptPassword(passwordForm.getPassword()));
                return user;
            } else {
                throw new UserNotFoundException("Username not found: " + passwordForm.getUsername());
            }
        } else {
            throw new InvalidPasswordException("Password and Confirm Password fields do not match");
        }
    }

    public User get(Integer id) {
        return userRepository.findById(id).get();
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    public List<User> findEnabledUsersByUsername(boolean enabled, String username) {
        return userRepository.findUsersByEnabledAndUsername(enabled, username);
    }
}
