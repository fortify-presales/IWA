package com.microfocus.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;

import com.microfocus.example.DataSeeder;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.microfocus.example.BaseIntegrationTest;
import com.microfocus.example.entity.User;
import com.microfocus.example.web.form.PasswordForm;
import com.microfocus.example.web.form.UserForm;

import com.warrenstrange.googleauth.GoogleAuthenticator;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest extends BaseIntegrationTest {

    @Autowired
    UserService userService;

    @Test
    public void a_userService_findById() {
        Optional<User> u = userService.findUserById(DataSeeder.TEST_USER1_ID);
        if (u.isPresent())
            assertThat(u.get().getUsername()).isEqualTo(DataSeeder.TEST_USER1_USERNAME);
        else
            fail("Test User 1 not found");
    }

    @Test
    public void b_userService_findUserByUsername() {
        Optional<User> u = userService.findUserByUsername(DataSeeder.TEST_USER1_USERNAME);
        if (u.isPresent())
            assertThat(u.get().getUsername()).isEqualTo(DataSeeder.TEST_USER1_USERNAME);
        else
            fail("Test User 1 not found");
    }

    @Test
    public void d_userService_save() {
        Optional<User> optionalUser = userService.findUserByUsername(DataSeeder.TEST_USER1_USERNAME);
        if (optionalUser.isPresent()) {
            UserForm userForm = new UserForm(optionalUser.get());
            userForm.setFirstName("Test");
            userForm.setLastName("User 1 Updated");
            try {
                userService.saveUserFromUserForm(userForm);
                Optional<User> updatedUser = userService.findUserByUsername(DataSeeder.TEST_USER1_USERNAME);
                updatedUser.ifPresent(user -> assertAll(
                    () -> assertEquals("Test", user.getFirstName()),
                    () -> assertEquals("User 1 Updated", user.getLastName()))
                );
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        } else
            fail("Test User 1 not found");
    }

    @Test
    public void e_userService_updatePassword() {
        Optional<User> optionalUser = userService.findUserByUsername(DataSeeder.TEST_USER1_USERNAME);
        if (optionalUser.isPresent()) {
            PasswordForm passwordForm = new PasswordForm(optionalUser.get());
            passwordForm.setPassword("password2");
            passwordForm.setConfirmPassword("password2");
            try {
                userService.updateUserPasswordFromPasswordForm(optionalUser.get().getId(), passwordForm);
                Optional<User> updatedUser = userService.findUserByUsername(DataSeeder.TEST_USER1_USERNAME);
                updatedUser.ifPresent(user -> assertThat(user.getFirstName()).isEqualTo("Test"));
            } catch (Exception ex) {
                fail(ex.getMessage());
            }
        } else
            fail("Test User 1 not found");
    }

    @Test
    public void a_userService_validateSecret() {
        Optional<User> u = userService.findUserById(DataSeeder.TEST_USER1_ID);
        if (u.isPresent())
            assertThat(u.get().getUsername()).isEqualTo(DataSeeder.TEST_USER1_USERNAME);
        else
            fail("Test User 1 not found");
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        String secretKey = u.get().getSecret();
        int code = gAuth.getTotpPassword(secretKey);
        assertTrue(gAuth.authorize(u.get().getSecret(), code));
    }
}
