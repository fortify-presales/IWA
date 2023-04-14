package com.microfocus.example.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class MessageTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link Message}
     *   <li>{@link Message#setId(UUID)}
     *   <li>{@link Message#setRead(Boolean)}
     *   <li>{@link Message#setReadDate(Date)}
     *   <li>{@link Message#setSentDate(Date)}
     *   <li>{@link Message#setText(String)}
     *   <li>{@link Message#setUser(User)}
     *   <li>{@link Message#toString()}
     *   <li>{@link Message#getId()}
     *   <li>{@link Message#getRead()}
     *   <li>{@link Message#getReadDate()}
     *   <li>{@link Message#getSentDate()}
     *   <li>{@link Message#getText()}
     *   <li>{@link Message#getUser()}
     * </ul>
     */
    @Test
    void testConstructor() {
        Message actualMessage = new Message();
        UUID randomUUIDResult = UUID.randomUUID();
        actualMessage.setId(randomUUIDResult);
        actualMessage.setRead(true);
        Date fromResult = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        actualMessage.setReadDate(fromResult);
        Date fromResult1 = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        actualMessage.setSentDate(fromResult1);
        actualMessage.setText("Text");
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
        user.setId(UUID.randomUUID());
        user.setLastName("Doe");
        user.setMfa(true);
        user.setPassword("iloveyou");
        user.setPhone("6625550144");
        user.setState("MD");
        user.setUsername("janedoe");
        user.setVerifyCode("Verify Code");
        user.setZip("21654");
        actualMessage.setUser(user);
        actualMessage.toString();
        assertSame(randomUUIDResult, actualMessage.getId());
        assertTrue(actualMessage.getRead());
        assertSame(fromResult, actualMessage.getReadDate());
        assertSame(fromResult1, actualMessage.getSentDate());
        assertEquals("Text", actualMessage.getText());
        assertSame(user, actualMessage.getUser());
    }

    /**
     * Method under test: default or parameterless constructor of {@link Message}
     */
    @Test
    void testConstructor2() {
        assertFalse((new Message()).getRead());
    }
}

