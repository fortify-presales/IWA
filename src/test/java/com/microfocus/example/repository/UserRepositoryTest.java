package com.microfocus.example.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.microfocus.example.BaseIntegrationTest;
import com.microfocus.example.DataSeeder;
import com.microfocus.example.entity.User;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserRepositoryTest extends BaseIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void a_userRepository_existsById() {
        if (!userRepository.existsById(1)) fail("Admin user does not exist");
    }

    @Test
    public void b_userRepository_findUserById() {
        Optional<User> u = userRepository.findById(1);
        if (u.isPresent())
            assertThat(u.get().getUsername()).isEqualTo("admin");
        else
            fail("Admin user not found");
    }

    @Test
    public void c_userRepository_findUserByUsername() {
        Optional<User> u = userRepository.findUserByUsername("test");
        if (u.isPresent())
            assertThat(u.get().getUsername()).isEqualTo("test");
        else
            fail("Test user not found");
    }

    @Test
    public void d_userRepository_findAll() {
        List<User> users = userRepository.findAll();
        assertThat(users.size()).isEqualTo(3L);
    }

    @Test
    public void e_userRepository_persist() {
        User u = DataSeeder.generateUser();
        userRepository.save(u);
        if (!userRepository.existsById(u.getId())) fail("Test user 2 does not exist");
        Optional<User> u2 = userRepository.findUserByUsername(u.getUsername());
        if (u2.isPresent())
            assertThat(u2.get().getUsername()).isEqualTo(u.getUsername());
        else
            fail("Test user 2 not found");
    }

    @Test
    public void f_userRepository_update() {
        Optional<User> optionalUser = userRepository.findUserByUsername("test");
        if (optionalUser.isPresent()) {
            assertThat(optionalUser.get().getUsername()).isEqualTo("test");
            User u = optionalUser.get();
            u.setName("test user updated");
            u.setEmail("testupdated@localhost");
            u.setMobile("0987654321");
            userRepository.save(u);
            Optional<User> u2 = userRepository.findUserByUsername("test");
            if (u2.isPresent()) {
                assertThat(u2.get().getName()).isEqualTo("test user updated");
                assertThat(u2.get().getEmail()).isEqualTo("testupdated@localhost");
                assertThat(u2.get().getMobile()).isEqualTo("0987654321");
            }
        } else
            fail("Test user not found");
    }
}
