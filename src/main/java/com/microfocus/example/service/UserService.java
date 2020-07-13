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

package com.microfocus.example.service;

import com.microfocus.example.entity.Authority;
import com.microfocus.example.entity.Message;
import com.microfocus.example.entity.User;
import com.microfocus.example.exception.InvalidPasswordException;
import com.microfocus.example.exception.UserNotFoundException;
import com.microfocus.example.repository.MessageRepository;
import com.microfocus.example.repository.RoleRepository;
import com.microfocus.example.repository.UserRepository;
import com.microfocus.example.utils.EncryptedPasswordUtils;
import com.microfocus.example.web.form.*;
import com.microfocus.example.web.form.admin.AdminNewUserForm;
import com.microfocus.example.web.form.admin.AdminPasswordForm;
import com.microfocus.example.web.form.admin.AdminUserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * User Service to hide business logs / database persistance
 * @author Kevin A. Lee
 */
@Service
@Transactional
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MessageRepository messageRepository;


    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).get();
    }

    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }

    public List<User> findUsersByUsername(String username) {
        return userRepository.findUsersByUsername(username);
    }

    public List<User> findEnabledUsersByUsername(boolean enabled, String username) {
        return userRepository.findUsersByEnabledAndUsername(enabled, username);
    }

    public boolean userExistsById(Integer id) {
        return userRepository.existsById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User saveUserFromUserForm(UserForm userForm) throws InvalidPasswordException, UserNotFoundException {
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

    public User saveUserFromAdminUserForm(AdminUserForm adminUserForm) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findUserByUsername(adminUserForm.getUsername());
        if (optionalUser.isPresent()) {
            User utmp = optionalUser.get();
            utmp.setUsername(adminUserForm.getUsername());
            utmp.setName(adminUserForm.getName());
            utmp.setEmail(adminUserForm.getEmail());
            utmp.setMobile(adminUserForm.getMobile());
            utmp.setEnabled(adminUserForm.getEnabled());
            return utmp;
        } else {
            throw new UserNotFoundException("Username not found: " + adminUserForm.getUsername());
        }
    }

    public User updateUserPasswordFromPasswordForm(Integer id, PasswordForm passwordForm) {
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

    public User updateUserPasswordFromAdminPasswordForm(Integer id, AdminPasswordForm adminPasswordForm) {
        if (adminPasswordForm.getPassword().equals(adminPasswordForm.getConfirmPassword())) {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setPassword(EncryptedPasswordUtils.encryptPassword(adminPasswordForm.getPassword()));
                return user;
            } else {
                throw new UserNotFoundException("Username not found: " + adminPasswordForm.getUsername());
            }
        } else {
            throw new InvalidPasswordException("Password and Confirm Password fields do not match");
        }
    }

    public User addUserFromAdminNewUserForm(AdminNewUserForm adminNewUserForm) {
        Set<Authority> authorities = new HashSet<Authority>();
        authorities.add(roleRepository.findByName("ROLE_USER").get());
        User utmp = new User();
        utmp.setUsername(adminNewUserForm.getUsername());
        utmp.setName(adminNewUserForm.getName());
        utmp.setPassword(EncryptedPasswordUtils.encryptPassword(adminNewUserForm.getPassword()));
        utmp.setEmail(adminNewUserForm.getEmail());
        utmp.setMobile(adminNewUserForm.getMobile());
        utmp.setEnabled(adminNewUserForm.getEnabled());
        utmp.setDateCreated(new Date());
        utmp.setAuthorities(authorities);
        User newUser = userRepository.saveAndFlush(utmp);
        return newUser;
    }

    //
    // User Roles
    //

    public List<Authority> getAllRoles() {
        return roleRepository.findAll();
    }

    //public List<Authority> getUserRoles(Integer userId) {
    //    return roleRepository.findByUserId(userId);
    //}

    public boolean roleExistsById(Integer id) {
        return roleRepository.existsById(id);
    }

    public Optional<Authority> findRoleById(Integer id) {
        return roleRepository.findById(id);
    }

    public Authority saveRole(Authority role) {
        return roleRepository.save(role);
    }

    public void deleteRoleById(Integer id) {
        roleRepository.deleteById(id);
    }

    //
    // User Messages
    //

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public long getUserMessageCount(Integer userId) {
        return messageRepository.countByUserId(userId);
    }

    public long getUserUnreadMessageCount(Integer userId) {
        return messageRepository.countUnreadByUserId(userId);
    }

    public List<Message> getUserMessages(Integer userId) {
        return messageRepository.findByUserId(userId);
    }

    public boolean messageExistsById(Integer id) {
        return messageRepository.existsById(id);
    }

    public Optional<Message> findMessageById(Integer id) {
        return messageRepository.findById(id);
    }

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public void deleteMessageById(Integer id) {
        messageRepository.deleteById(id);
    }

    public void markMessageAsReadById(Integer id) { messageRepository.markMessageAsReadById(id); }

}
