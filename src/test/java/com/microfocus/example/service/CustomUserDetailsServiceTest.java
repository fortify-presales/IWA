package com.microfocus.example.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.microfocus.example.entity.User;
import com.microfocus.example.repository.UserRepositoryCustom;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CustomUserDetailsService.class})
@ExtendWith(SpringExtension.class)
class CustomUserDetailsServiceTest {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private UserRepositoryCustom userRepositoryCustom;

    /**
     * Method under test: {@link CustomUserDetailsService#loadUserByUsername(String)}
     */
    @Test
    void testLoadUserByUsername() throws UsernameNotFoundException {
        User user = new User();
        user.setAddress("42 Main St");
        user.setAuthorities(new HashSet<>());
        user.setCity("Oxford");
        user.setConfirmPassword("iloveyou");
        user.setCountry("GB");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        user.setDateCreated(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setEnabled(true);
        user.setFirstName("Jane");
        user.setId(UUID.randomUUID());
        user.setLastName("Doe");
        user.setMfa(true);
        user.setPassword("iloveyou");
        user.setPhone("4105551212");
        user.setState("MD");
        user.setUsername("janedoe");
        user.setVerifyCode("Verify Code");
        user.setZip("21654");
        Optional<User> ofResult = Optional.of(user);

        User user1 = new User();
        user1.setAddress("42 Main St");
        user1.setAuthorities(new HashSet<>());
        user1.setCity("Oxford");
        user1.setConfirmPassword("iloveyou");
        user1.setCountry("GB");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        user1.setDateCreated(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        user1.setEmail("jane.doe@example.org");
        user1.setEnabled(true);
        user1.setFirstName("Jane");
        user1.setId(UUID.randomUUID());
        user1.setLastName("Doe");
        user1.setMfa(true);
        user1.setPassword("iloveyou");
        user1.setPhone("4105551212");
        user1.setState("MD");
        user1.setUsername("janedoe");
        user1.setVerifyCode("Verify Code");
        user1.setZip("21654");
        Optional<User> ofResult1 = Optional.of(user1);
        when(userRepositoryCustom.findUserByEmail((String) any())).thenReturn(ofResult);
        when(userRepositoryCustom.findUserByUsername((String) any())).thenReturn(ofResult1);
        assertTrue(customUserDetailsService.loadUserByUsername("janedoe").isEnabled());
        verify(userRepositoryCustom).findUserByUsername((String) any());
    }
}

