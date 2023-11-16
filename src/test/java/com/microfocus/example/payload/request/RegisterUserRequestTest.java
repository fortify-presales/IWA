package com.microfocus.example.payload.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class RegisterUserRequestTest {
    /**
     * Method under test: {@link RegisterUserRequest#passwordEncoder()}
     */
    @Test
    void testPasswordEncoder() {
        assertFalse((new RegisterUserRequest()).passwordEncoder().upgradeEncoding("secret"));
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link RegisterUserRequest}
     *   <li>{@link RegisterUserRequest#setEmail(String)}
     *   <li>{@link RegisterUserRequest#setFirstName(String)}
     *   <li>{@link RegisterUserRequest#setLastName(String)}
     *   <li>{@link RegisterUserRequest#setPassword(String)}
     *   <li>{@link RegisterUserRequest#setPhone(String)}
     *   <li>{@link RegisterUserRequest#setUsername(String)}
     *   <li>{@link RegisterUserRequest#toString()}
     *   <li>{@link RegisterUserRequest#getEmail()}
     *   <li>{@link RegisterUserRequest#getFirstName()}
     *   <li>{@link RegisterUserRequest#getLastName()}
     *   <li>{@link RegisterUserRequest#getPassword()}
     *   <li>{@link RegisterUserRequest#getPhone()}
     *   <li>{@link RegisterUserRequest#getUsername()}
     * </ul>
     */
    @Test
    void testConstructor() {
        RegisterUserRequest actualRegisterUserRequest = new RegisterUserRequest();
        actualRegisterUserRequest.setEmail("jane.doe@example.org");
        actualRegisterUserRequest.setFirstName("Jane");
        actualRegisterUserRequest.setLastName("Doe");
        actualRegisterUserRequest.setPassword("iloveyou");
        actualRegisterUserRequest.setPhone("6625550144");
        actualRegisterUserRequest.setUsername("janedoe");
        String actualToStringResult = actualRegisterUserRequest.toString();
        assertEquals("jane.doe@example.org", actualRegisterUserRequest.getEmail());
        assertEquals("Jane", actualRegisterUserRequest.getFirstName());
        assertEquals("Doe", actualRegisterUserRequest.getLastName());
        assertEquals("iloveyou", actualRegisterUserRequest.getPassword());
        assertEquals("6625550144", actualRegisterUserRequest.getPhone());
        assertEquals("janedoe", actualRegisterUserRequest.getUsername());
        assertEquals("RegisterUserRequest{, username='janedoe', email =jane.doe@example.org}", actualToStringResult);
    }
}

