package com.microfocus.example.service;

import com.microfocus.example.BaseIntegrationTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.microfocus.example.entity.User;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class UserServiceTest extends BaseIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    UserService userService;

    @Test
    public void listAll() {
        List<User> users = userService.listAll();
        assertThat(users.size()).isEqualTo(4L);
    }
}
