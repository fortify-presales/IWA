package com.microfocus.example;

import com.microfocus.example.entity.Authority;
import com.microfocus.example.entity.AuthorityType;
import com.microfocus.example.entity.User;

/**
 * A simple data seeder for domain objects.
 *
 * @author Kevin A. Lee
 */
public class DataSeeder {

    public static final String TEST_USER_USERNAME = "test2";
    public static final String TEST_USER_NAME = "test user 2";
    public static final String TEST_USER_PASSWORD = "password";
    public static final String TEST_USER_EMAIL = "test2@localhost";
    public static final String TEST_USER_MOBILE = "0123456789";


    public static User generateUser() {
        User user = new User();
        user.setUsername(TEST_USER_USERNAME);
        user.setPassword(TEST_USER_PASSWORD);
        user.setName(TEST_USER_NAME);
        user.setEmail(TEST_USER_EMAIL);
        user.setMobile(TEST_USER_MOBILE);
        // no roles
        return user;
    }

    public static Authority generateAdminRole() {
        Authority role = new Authority();
        role.setName(AuthorityType.ROLE_ADMIN);
        return role;
    }

    public static Authority generateUserRole() {
        Authority role = new Authority();
        role.setName(AuthorityType.ROLE_USER);
        return role;
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
