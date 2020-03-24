package com.microfocus.example.service;

import com.microfocus.example.entity.User;
import com.microfocus.example.exception.InvalidPasswordException;
import com.microfocus.example.exception.UserNotFoundException;
import com.microfocus.example.repository.IUserRepository;
import com.microfocus.example.utils.EncryptedPasswordUtils;
import com.microfocus.example.web.form.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
            if (!EncryptedPasswordUtils.matches(userForm.getConfirmPassword(), user.getPassword()))
                throw new InvalidPasswordException("Password is incorrect");
            user.setName(userForm.getName());
            user.setEmail(userForm.getEmail());
            user.setMobile(userForm.getMobile());
            return user;
        } else {
            throw new UserNotFoundException("Username not found: " + userForm.getUsername());
        }
    }

    public User get(Integer id) {
        return userRepository.findById(id).get();
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
}
