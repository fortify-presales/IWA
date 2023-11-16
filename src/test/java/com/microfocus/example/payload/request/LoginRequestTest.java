package com.microfocus.example.payload.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LoginRequestTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link LoginRequest}
     *   <li>{@link LoginRequest#setPassword(String)}
     *   <li>{@link LoginRequest#setUsername(String)}
     *   <li>{@link LoginRequest#getPassword()}
     *   <li>{@link LoginRequest#getUsername()}
     * </ul>
     */
    @Test
    void testConstructor() {
        LoginRequest actualLoginRequest = new LoginRequest();
        actualLoginRequest.setPassword("iloveyou");
        actualLoginRequest.setUsername("janedoe");
        assertEquals("iloveyou", actualLoginRequest.getPassword());
        assertEquals("janedoe", actualLoginRequest.getUsername());
    }
}

