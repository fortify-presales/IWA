package com.microfocus.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.microfocus.example.BaseIntegrationTest;
import com.microfocus.example.entity.User;
import com.microfocus.example.web.form.PasswordForm;
import com.microfocus.example.web.form.UserForm;


public class UserServiceTest extends BaseIntegrationTest {

    @Autowired
    UserService userService;

    @Test
    public void userService_findById() {
        Optional<User> u = userService.findById(1);
        if (u.isPresent())
            assertThat(u.get().getUsername()).isEqualTo("admin");
        else
            fail("Admin user not found");
    }

    @Test
    public void userService_findUserByUsername() {
        Optional<User> u = userService.findUserByUsername("test");
        if (u.isPresent())
            assertThat(u.get().getUsername()).isEqualTo("test");
        else
            fail("Test user not found");
    }

    @Test
    public void userService_listAll() {
        List<User> users = userService.listAll();
        assertThat(users.size()).isEqualTo(4L);
    }

    @Test
    public void userService_save() {
        Optional<User> optionalUser = userService.findUserByUsername("test");
        if (optionalUser.isPresent()) {
            UserForm userForm = new UserForm(optionalUser.get());
            userForm.setName("Test User Updated");
            try {
                userService.save(userForm);
                Optional<User> updatedUser = userService.findUserByUsername("test");
                if (updatedUser.isPresent()) {
                    assertThat(updatedUser.get().getName()).isEqualTo("Test User Updated");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        } else
            fail("Test user not found");
    }

    @Test
    public void userService_updatePassword() {
        Optional<User> optionalUser = userService.findUserByUsername("test");
        if (optionalUser.isPresent()) {
            PasswordForm passwordForm = new PasswordForm(optionalUser.get());
            passwordForm.setPassword("password2");
            passwordForm.setConfirmPassword("password2");
            try {
                userService.updatePassword(optionalUser.get().getId(), passwordForm);
                Optional<User> updatedUser = userService.findUserByUsername("test");
                if (updatedUser.isPresent()) {
                    assertThat(updatedUser.get().getName()).isEqualTo("test user updated");
                }
            } catch (Exception ex) {
                fail(ex.getMessage());
            }
        } else
            fail("Test user not found");
    }
}
