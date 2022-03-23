/*
        Insecure Web App (IWA)

        Copyright (C) 2021 Micro Focus or one of its affiliates

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
import com.microfocus.example.entity.Order;
import com.microfocus.example.entity.User;
import com.microfocus.example.exception.*;
import com.microfocus.example.payload.request.MessageRequest;
import com.microfocus.example.payload.request.RegisterUserRequest;
import com.microfocus.example.payload.request.SubscribeUserRequest;
import com.microfocus.example.payload.response.RegisterUserResponse;
import com.microfocus.example.payload.response.SubscribeUserResponse;
import com.microfocus.example.repository.MessageRepository;
import com.microfocus.example.repository.OrderRepository;
import com.microfocus.example.repository.RoleRepository;
import com.microfocus.example.repository.UserRepository;
import com.microfocus.example.utils.EncryptedPasswordUtils;
import com.microfocus.example.utils.UserUtils;
import com.microfocus.example.web.form.*;
import com.microfocus.example.web.form.admin.AdminNewUserForm;
import com.microfocus.example.web.form.admin.AdminPasswordForm;
import com.microfocus.example.web.form.admin.AdminUserForm;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

/**
 * User Service to hide business logs / database persistence
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

    @Autowired
    private OrderRepository orderRepository;

    @Value("${app.data.page-size:25}")
    private Integer pageSize;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public Optional<User> findUserByEmail(String email) { return userRepository.findUserByEmail(email); }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public List<User> getAllUsers(Integer offset, String keywords) {
        if (keywords != null && !keywords.isEmpty()) {
            return userRepository.findUsersByKeywords(keywords, offset, pageSize);
        } else {
            return userRepository.listUsers(offset, pageSize);
        }
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).get();
    }

    public void deleteUserById(UUID id) {
        userRepository.deleteById(id);
    }

    public List<User> findUsersByUsername(String username) {
        return userRepository.findUsersByUsername(username);
    }

    public List<User> findEnabledUsersByUsername(boolean enabled, String username) {
        return userRepository.findUsersByEnabledAndUsername(enabled, username);
    }

    public boolean userExistsById(UUID id) {
        return userRepository.existsById(id);
    }

    public User registerUser(RegisterUserForm newUser) throws UsernameTakenException, EmailAddressTakenException {
        if (findUserByUsername(newUser.getUsername()).isPresent()) {
            throw new UsernameTakenException(newUser.getUsername());
        }
        if (findUserByEmail(newUser.getEmail()).isPresent()) {
            throw new EmailAddressTakenException(newUser.getEmail());
        }
        Set<Authority> authorities = new HashSet<Authority>();
        authorities.add(roleRepository.findByName("ROLE_USER").get());
        User utmp = new User();
        utmp.setUsername(newUser.getUsername());
        utmp.setFirstName(newUser.getFirstName());
        utmp.setLastName(newUser.getLastName());
        utmp.setPassword(EncryptedPasswordUtils.encryptPassword(newUser.getPassword()));
        utmp.setEmail(newUser.getEmail());
        utmp.setPhone(newUser.getPhone());
        utmp.setEnabled(true);
        utmp.setDateCreated(new Date());
        utmp.setAuthorities(authorities);
        userRepository.saveAndFlush(utmp);
        return utmp;
    }

    public RegisterUserResponse registerUser(RegisterUserRequest newUser) throws UsernameTakenException, EmailAddressTakenException {
        if (findUserByUsername(newUser.getUsername()).isPresent()) {
            throw new UsernameTakenException(newUser.getUsername());
        }
        if (findUserByEmail(newUser.getEmail()).isPresent()) {
            throw new EmailAddressTakenException(newUser.getEmail());
        }
        Set<Authority> authorities = new HashSet<Authority>();
        authorities.add(roleRepository.findByName("ROLE_USER").get());
        User utmp = new User();
        utmp.setUsername(newUser.getUsername());
        utmp.setFirstName(newUser.getFirstName());
        utmp.setLastName(newUser.getLastName());
        utmp.setPassword(EncryptedPasswordUtils.encryptPassword(newUser.getPassword()));
        utmp.setEmail(newUser.getEmail());
        utmp.setPhone(newUser.getPhone());
        utmp.setEnabled(true);
        utmp.setDateCreated(new Date());
        utmp.setAuthorities(authorities);
        userRepository.saveAndFlush(utmp);
        RegisterUserResponse registeredUser = new RegisterUserResponse(utmp.getUsername(), utmp.getPassword(),
                newUser.getFirstName(), utmp.getLastName(), utmp.getEmail(), utmp.getPhone());
        return registeredUser;
    }

    public SubscribeUserResponse subscribeUser(SubscribeUserRequest newUser) {
        try {
            UserUtils.registerUser(null, null, newUser.getEmail());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        SubscribeUserResponse subscribedUser = new SubscribeUserResponse(newUser.getId(), null, null, newUser.getEmail());
        return subscribedUser;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User saveUserFromApi(User apiUser) throws InvalidPasswordException, UserNotFoundException {
        Optional<User> optionalUser = userRepository.findUserByUsername(apiUser.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!apiUser.getPassword().isEmpty()) {
                if (!EncryptedPasswordUtils.matches(apiUser.getPassword(), user.getPassword())) {
                    throw new InvalidPasswordException("Password is incorrect");
                }
            }
            user.setFirstName(apiUser.getFirstName());
            user.setLastName(apiUser.getLastName());
            user.setEmail(apiUser.getEmail());
            user.setPhone(apiUser.getPhone());
            user.setAddress(apiUser.getAddress());
            user.setCity(apiUser.getCity());
            user.setState(apiUser.getState());
            user.setZip(apiUser.getZip());
            user.setCountry(apiUser.getCountry());
            return userRepository.saveAndFlush(user);
        } else {
            throw new UserNotFoundException("Username not found: " + apiUser.getUsername());
        }
    }

    public User saveUserFromUserForm(UserForm userForm) throws InvalidPasswordException, UserNotFoundException {
        Optional<User> optionalUser = userRepository.findUserByUsername(userForm.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!EncryptedPasswordUtils.matches(userForm.getPassword(), user.getPassword())) {
                throw new InvalidPasswordException("Password is incorrect");
            }
            user.setFirstName(userForm.getFirstName());
            user.setLastName(userForm.getLastName());
            user.setEmail(userForm.getEmail());
            user.setPhone(userForm.getPhone());
            user.setAddress(user.getAddress());
            user.setCity(user.getCity());
            user.setState(user.getState());
            user.setZip(user.getZip());
            user.setCountry(user.getCountry());
            return userRepository.saveAndFlush(user);
        } else {
            throw new UserNotFoundException("Username not found: " + userForm.getUsername());
        }
    }

    public User saveUserFromAdminUserForm(AdminUserForm adminUserForm) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findUserByUsername(adminUserForm.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setId(adminUserForm.getId());
            user.setUsername(adminUserForm.getUsername());
            user.setFirstName(adminUserForm.getFirstName());
            user.setLastName(adminUserForm.getLastName());
            user.setEmail(adminUserForm.getEmail());
            user.setPhone(adminUserForm.getPhone());
            user.setAddress(adminUserForm.getAddress());
            user.setCity(adminUserForm.getCity());
            user.setState(adminUserForm.getState());
            user.setZip(adminUserForm.getZip());
            user.setCountry(adminUserForm.getCountry());
            user.setEnabled(adminUserForm.getEnabled());
            return userRepository.saveAndFlush(user);
        } else {
            throw new UserNotFoundException("Username not found: " + adminUserForm.getUsername());
        }
    }

    public User updateUserPasswordFromPasswordForm(UUID id, PasswordForm passwordForm) {
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

    public User updateUserPasswordFromAdminPasswordForm(UUID id, AdminPasswordForm adminPasswordForm) {
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
        utmp.setFirstName(adminNewUserForm.getFirstName());
        utmp.setLastName(adminNewUserForm.getLastName());
        utmp.setPassword(EncryptedPasswordUtils.encryptPassword(adminNewUserForm.getPassword()));
        utmp.setEmail(adminNewUserForm.getEmail());
        utmp.setPhone(adminNewUserForm.getPhone());
        utmp.setAddress(adminNewUserForm.getAddress());
        utmp.setCity(adminNewUserForm.getCity());
        utmp.setState(adminNewUserForm.getState());
        utmp.setZip(adminNewUserForm.getZip());
        utmp.setCountry(adminNewUserForm.getCountry());
        utmp.setEnabled(adminNewUserForm.getEnabled());
        utmp.setEnabled(adminNewUserForm.getEnabled());
        utmp.setDateCreated(new Date());
        utmp.setAuthorities(authorities);
        return userRepository.saveAndFlush(utmp);
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

    public long getUserMessageCount(UUID userId) {
        return messageRepository.countByUserId(userId);
    }

    public long getUserUnreadMessageCount(UUID userId) {
        return messageRepository.countUnreadByUserId(userId);
    }

    public List<Message> getUserMessages(UUID userId) {
        return messageRepository.findByUserId(userId);
    }

    public boolean messageExistsById(UUID id) {
        return messageRepository.existsById(id);
    }

    public Optional<Message> findMessageById(UUID id) {
        return messageRepository.findById(id);
    }

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message saveMessage(MessageForm message) {
        return messageRepository.save(message);
    }

    public Message saveMessageFromApi(UUID messageId, MessageRequest message) throws MessageNotFoundException, UserNotFoundException {
        Message mtmp = new Message();
        Optional<User> optionalUser = userRepository.findById(message.getUserId());
        if (optionalUser.isPresent()) {
            mtmp.setUser(optionalUser.get());
            // are we creating a new message or updating an existing message?
            if (messageId == null) {
                mtmp.setId(null);
            } else {
                mtmp.setId(messageId);
                // check it exists
                if (!messageExistsById(messageId))
                    throw new MessageNotFoundException("Message not found with id: " + messageId);
            }
            mtmp.setText(message.getText());
            if (message.getSentDate() != null) mtmp.setSentDate(message.getSentDate());
            return messageRepository.save(mtmp);
        } else {
            throw new UserNotFoundException("User not found with id: " + message.getUserId());
        }
    }

    public void deleteMessageById(UUID id) {
        messageRepository.deleteById(id);
    }

    public void markMessageAsReadById(UUID id) { messageRepository.markMessageAsReadById(id); }

    //
    // User Orders
    //

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public long getUserOrderCount(UUID userId) {
        return orderRepository.countByUserId(userId);
    }

    public long getUserUnshippedOrderCount(UUID userId) {
        return orderRepository.countNotShippedByUserId(userId);
    }

    public List<Order> getUserOrders(UUID userId) {
        return orderRepository.findByUserId(userId);
    }

    public boolean orderExistsById(UUID id) {
        return orderRepository.existsById(id);
    }

    public Optional<Order> findOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    /*
    public Message saveOrder(MessageForm message) {
        return messageRepository.save(message);
    }


    public Message saveMessageFromApi(UUID messageId, MessageRequest message) throws MessageNotFoundException, UserNotFoundException {
        Message mtmp = new Message();
        Optional<User> optionalUser = userRepository.findById(message.getUserId());
        if (optionalUser.isPresent()) {
            mtmp.setUser(optionalUser.get());
            // are we creating a new message or updating an existing message?
            if (messageId == null) {
                mtmp.setId(null);
            } else {
                mtmp.setId(messageId);
                // check it exists
                if (!messageExistsById(messageId))
                    throw new MessageNotFoundException("Message not found with id: " + messageId);
            }
            mtmp.setText(message.getText());
            if (message.getSentDate() != null) mtmp.setSentDate(message.getSentDate());
            return messageRepository.save(mtmp);
        } else {
            throw new UserNotFoundException("User not found with id: " + message.getUserId());
        }
    }

    public void deleteMessageById(UUID id) {
        messageRepository.deleteById(id);
    }

    public void markMessageAsReadById(UUID id) { messageRepository.markMessageAsReadById(id); }

    */
}
