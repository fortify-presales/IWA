package com.microfocus.example.payload.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SubscribeUserRequestTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link SubscribeUserRequest}
     *   <li>{@link SubscribeUserRequest#setEmail(String)}
     *   <li>{@link SubscribeUserRequest#setFirstName(String)}
     *   <li>{@link SubscribeUserRequest#setId(Integer)}
     *   <li>{@link SubscribeUserRequest#setLastName(String)}
     *   <li>{@link SubscribeUserRequest#toString()}
     *   <li>{@link SubscribeUserRequest#getEmail()}
     *   <li>{@link SubscribeUserRequest#getFirstName()}
     *   <li>{@link SubscribeUserRequest#getId()}
     *   <li>{@link SubscribeUserRequest#getLastName()}
     * </ul>
     */
    @Test
    void testConstructor() {
        SubscribeUserRequest actualSubscribeUserRequest = new SubscribeUserRequest();
        actualSubscribeUserRequest.setEmail("jane.doe@example.org");
        actualSubscribeUserRequest.setFirstName("Jane");
        actualSubscribeUserRequest.setId(1);
        actualSubscribeUserRequest.setLastName("Doe");
        String actualToStringResult = actualSubscribeUserRequest.toString();
        assertEquals("jane.doe@example.org", actualSubscribeUserRequest.getEmail());
        assertEquals("Jane", actualSubscribeUserRequest.getFirstName());
        assertEquals(1, actualSubscribeUserRequest.getId().intValue());
        assertEquals("Doe", actualSubscribeUserRequest.getLastName());
        assertEquals("SubscribeUserRequest{id='1', firstName='Jane', lastName='Doe', email=jane.doe@example.org}",
                actualToStringResult);
    }
}

