package com.microfocus.example.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.microfocus.example.entity.Authority;
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

    @Autowired
    RoleRepository roleRepository;

    @Test
    public void a_userRepository_existsById() {
        if (!userRepository.existsById(DataSeeder.TEST_USER1_ID)) fail("Test User 1 does not exist");
    }

    @Test
    public void b_userRepository_findUserById() {
        Optional<User> u = userRepository.findById(DataSeeder.TEST_USER1_ID);
        if (u.isPresent())
            assertThat(u.get().getUsername()).isEqualTo(DataSeeder.TEST_USER1_USERNAME);
        else
            fail("Test User 1 user not found");
    }

    @Test
    public void c_userRepository_findUserByUsername() {
        Optional<User> u = userRepository.findUserByUsername(DataSeeder.TEST_USER1_USERNAME);
        if (u.isPresent())
            assertThat(u.get().getUsername()).isEqualTo(DataSeeder.TEST_USER1_USERNAME);
        else
            fail("Test User 1 not found");
    }

    @Test
    public void d_userRepository_persist() {
        User u = DataSeeder.generateUser();
        u = userRepository.saveAndFlush(u);
        //if (!userRepository.existsById(u.getId())) fail("Test user 2 does not exist");
        List<User> users = userRepository.findUsersByUsername(DataSeeder.TEST_USER2_USERNAME);
        if (users.size() > 0)
            assertThat(users.get(0).getUsername()).isEqualTo(DataSeeder.TEST_USER2_USERNAME);
        else
            fail("Test User 2 not found");
    }

    @Test
    public void f_userRepository_addAuthorities() {
        Authority a = null;
        Optional<Authority> optionalRole = roleRepository.findByName("ROLE_USER");
        if (optionalRole.isPresent()) {
            a = optionalRole.get();
        } else {
            fail("ROLE_USER not found");
        }
        System.out.println(a.toString());
        Set<Authority> authorities = new HashSet<Authority>();
        authorities.add(a);
        List<User> users = userRepository.findUsersByUsername(DataSeeder.TEST_USER2_USERNAME);
        if (users.size() > 0) {
            User u = users.get(0);
            u.setAuthorities(authorities);
            userRepository.save(u);
        } else {
            fail("Test User 2 not found");
        }
    }

    @Test
    public void e_userRepository_update() {
        List<User> users = userRepository.findUsersByUsername(DataSeeder.TEST_USER2_USERNAME);
        if (users.size() > 0) {
            User u = users.get(0);
            assertThat(u.getUsername()).isEqualTo(DataSeeder.TEST_USER2_USERNAME);
            u.setFirstName("Test Updated");
            u.setEmail("test2@updated.com");
            u.setPhone("0987654321");
            userRepository.saveAndFlush(u);
            users = userRepository.findUsersByUsername(DataSeeder.TEST_USER2_USERNAME);
            if (users.size() > 0) {
                u = users.get(0);
                assertThat(u.getFirstName()).isEqualTo("Test Updated");
                assertThat(u.getEmail()).isEqualTo("test2@updated.com");
                assertThat(u.getPhone()).isEqualTo("0987654321");
            }
        } else
            fail("Test User 2 not found");
    }


}
