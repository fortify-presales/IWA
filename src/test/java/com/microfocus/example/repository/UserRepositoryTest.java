package com.microfocus.example.repository;

import com.microfocus.example.BaseIntegrationTest;
import com.microfocus.example.DataSeeder;
import com.microfocus.example.entity.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class UserRepositoryTest extends BaseIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(UserRepositoryTest.class);

    @Autowired
    IUserRepository userRepository;

    @Test
    public void userRepository_existsById() {
        if (!userRepository.existsById(1)) fail("Admin user does not exist");
    }

    @Test
    public void userRepository_findUserById() {
        Optional<User> u = userRepository.findById(1);
        if (u.isPresent())
            assertThat(u.get().getUsername()).isEqualTo("admin");
        else
            fail("Admin user not found");
    }

    @Test
    public void userRepository_findUserByUsername() {
        Optional<User> u = userRepository.findUserByUsername("test");
        if (u.isPresent())
            assertThat(u.get().getUsername()).isEqualTo("test");
        else
            fail("Test user not found");
    }

    @Test
    public void userRepository_findAll() {
        List<User> users = userRepository.findAll();
        assertThat(users.size()).isEqualTo(3L);
    }

    @Test
    public void userRepository_persist() {
        User u = DataSeeder.generateUser();
        userRepository.save(u);
        if (!userRepository.existsById(u.getId())) fail("Test user 2 does not exist");
        Optional<User> u2 = userRepository.findUserByUsername(u.getUsername());
        if (u2.isPresent())
            assertThat(u2.get().getUsername()).isEqualTo(u.getUsername());
        else
            fail("Test user 2 not found");
    }
}
