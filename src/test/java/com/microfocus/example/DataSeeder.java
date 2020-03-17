package com.microfocus.example;

import com.microfocus.example.entity.Authority;
import com.microfocus.example.entity.AuthorityType;
import com.microfocus.example.entity.User;

import java.util.Collections;
import java.util.Set;

/**
 * A simple data seeder for domain objects.
 *
 * @author Kevin A. Lee
 */
public class DataSeeder {

    public static final String TEST_USER_USERNAME = "test3";
    public static final String TEST_USER_NAME = "Test User Three";
    public static final String TEST_USER_PASSWORD = "password";

    public static User generateUser() {
        User user = new User();
        user.setUsername(TEST_USER_USERNAME);
        user.setPassword(TEST_USER_PASSWORD);
        Set<Authority> roles =  Collections.emptySet();
        roles.add(generateCustomerRole());
        roles.add(generateSupervisorRole());
        user.setAuthorities(roles);
        return user;
    }

    public static Authority generateCustomerRole() {
        Authority role = new Authority();
        role.setName(AuthorityType.ROLE_CUSTOMER);
        return role;
    }

    public static Authority generateSupervisorRole() {
        Authority role = new Authority();
        role.setName(AuthorityType.ROLE_SUPERVISOR);
        return role;
    }

    public static Authority generateGuestRole() {
        Authority role = new Authority();
        role.setName(AuthorityType.ROLE_GUEST);
        return role;
    }

}
