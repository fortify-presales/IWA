package com.microfocus.example.payload.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.microfocus.example.entity.User;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class UserRequestTest {
    /**
     * Method under test: {@link UserRequest#passwordEncoder()}
     */
    @Test
    void testPasswordEncoder() {
        assertFalse((new UserRequest()).passwordEncoder().upgradeEncoding("secret"));
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link UserRequest#UserRequest()}
     *   <li>{@link UserRequest#setAddress(String)}
     *   <li>{@link UserRequest#setCity(String)}
     *   <li>{@link UserRequest#setConfirmPassword(String)}
     *   <li>{@link UserRequest#setCountry(String)}
     *   <li>{@link UserRequest#setEmail(String)}
     *   <li>{@link UserRequest#setEnabled(Boolean)}
     *   <li>{@link UserRequest#setFirstName(String)}
     *   <li>{@link UserRequest#setId(UUID)}
     *   <li>{@link UserRequest#setLastName(String)}
     *   <li>{@link UserRequest#setMfa(Boolean)}
     *   <li>{@link UserRequest#setPassword(String)}
     *   <li>{@link UserRequest#setPhone(String)}
     *   <li>{@link UserRequest#setState(String)}
     *   <li>{@link UserRequest#setUsername(String)}
     *   <li>{@link UserRequest#setZip(String)}
     *   <li>{@link UserRequest#toString()}
     *   <li>{@link UserRequest#getAddress()}
     *   <li>{@link UserRequest#getCity()}
     *   <li>{@link UserRequest#getConfirmPassword()}
     *   <li>{@link UserRequest#getCountry()}
     *   <li>{@link UserRequest#getEmail()}
     *   <li>{@link UserRequest#getEnabled()}
     *   <li>{@link UserRequest#getFirstName()}
     *   <li>{@link UserRequest#getId()}
     *   <li>{@link UserRequest#getLastName()}
     *   <li>{@link UserRequest#getMfa()}
     *   <li>{@link UserRequest#getPassword()}
     *   <li>{@link UserRequest#getPhone()}
     *   <li>{@link UserRequest#getState()}
     *   <li>{@link UserRequest#getUsername()}
     *   <li>{@link UserRequest#getZip()}
     * </ul>
     */
    @Test
    void testConstructor() {
        UserRequest actualUserRequest = new UserRequest();
        actualUserRequest.setAddress("42 Main St");
        actualUserRequest.setCity("Oxford");
        actualUserRequest.setConfirmPassword("iloveyou");
        actualUserRequest.setCountry("GB");
        actualUserRequest.setEmail("jane.doe@example.org");
        actualUserRequest.setEnabled(true);
        actualUserRequest.setFirstName("Jane");
        UUID randomUUIDResult = UUID.randomUUID();
        actualUserRequest.setId(randomUUIDResult);
        actualUserRequest.setLastName("Doe");
        actualUserRequest.setMfa(true);
        actualUserRequest.setPassword("iloveyou");
        actualUserRequest.setPhone("6625550144");
        actualUserRequest.setState("MD");
        actualUserRequest.setUsername("janedoe");
        actualUserRequest.setZip("21654");
        actualUserRequest.toString();
        assertEquals("42 Main St", actualUserRequest.getAddress());
        assertEquals("Oxford", actualUserRequest.getCity());
        assertEquals("iloveyou", actualUserRequest.getConfirmPassword());
        assertEquals("GB", actualUserRequest.getCountry());
        assertEquals("jane.doe@example.org", actualUserRequest.getEmail());
        assertTrue(actualUserRequest.getEnabled());
        assertEquals("Jane", actualUserRequest.getFirstName());
        assertSame(randomUUIDResult, actualUserRequest.getId());
        assertEquals("Doe", actualUserRequest.getLastName());
        assertTrue(actualUserRequest.getMfa());
        assertEquals("iloveyou", actualUserRequest.getPassword());
        assertEquals("6625550144", actualUserRequest.getPhone());
        assertEquals("MD", actualUserRequest.getState());
        assertEquals("janedoe", actualUserRequest.getUsername());
        assertEquals("21654", actualUserRequest.getZip());
    }

    /**
     * Method under test: {@link UserRequest#UserRequest(User)}
     */
    @Test
    void testConstructor2() {
        User user = new User();
        user.setAddress("42 Main St");
        user.setAuthorities(new HashSet<>());
        user.setCity("Oxford");
        user.setConfirmPassword("iloveyou");
        user.setCountry("GB");
        user.setDateCreated(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setEnabled(true);
        user.setFirstName("Jane");
        UUID randomUUIDResult = UUID.randomUUID();
        user.setId(randomUUIDResult);
        user.setLastName("Doe");
        user.setMfa(true);
        user.setPassword("iloveyou");
        user.setPhone("6625550144");
        user.setState("MD");
        user.setUsername("janedoe");
        user.setVerifyCode("Verify Code");
        user.setZip("21654");
        UserRequest actualUserRequest = new UserRequest(user);
        assertEquals("42 Main St", actualUserRequest.getAddress());
        assertEquals("21654", actualUserRequest.getZip());
        assertEquals("janedoe", actualUserRequest.getUsername());
        assertEquals("MD", actualUserRequest.getState());
        assertEquals("6625550144", actualUserRequest.getPhone());
        assertTrue(actualUserRequest.getMfa());
        assertEquals("Doe", actualUserRequest.getLastName());
        assertSame(randomUUIDResult, actualUserRequest.getId());
        assertEquals("Jane", actualUserRequest.getFirstName());
        assertEquals("Oxford", actualUserRequest.getCity());
        assertEquals("GB", actualUserRequest.getCountry());
        assertEquals("jane.doe@example.org", actualUserRequest.getEmail());
        assertTrue(actualUserRequest.getEnabled());
    }
}

