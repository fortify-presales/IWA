package com.microfocus.example.payload.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.microfocus.example.entity.Message;
import com.microfocus.example.entity.User;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class MessageRequestTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link MessageRequest#MessageRequest()}
     *   <li>{@link MessageRequest#setId(Integer)}
     *   <li>{@link MessageRequest#setReadDate(Date)}
     *   <li>{@link MessageRequest#setSentDate(Date)}
     *   <li>{@link MessageRequest#setText(String)}
     *   <li>{@link MessageRequest#setUserId(UUID)}
     *   <li>{@link MessageRequest#getId()}
     *   <li>{@link MessageRequest#getReadDate()}
     *   <li>{@link MessageRequest#getSentDate()}
     *   <li>{@link MessageRequest#getText()}
     *   <li>{@link MessageRequest#getUserId()}
     * </ul>
     */
    @Test
    void testConstructor() {
        MessageRequest actualMessageRequest = new MessageRequest();
        actualMessageRequest.setId(1);
        Date fromResult = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        actualMessageRequest.setReadDate(fromResult);
        Date fromResult1 = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        actualMessageRequest.setSentDate(fromResult1);
        actualMessageRequest.setText("Text");
        UUID randomUUIDResult = UUID.randomUUID();
        actualMessageRequest.setUserId(randomUUIDResult);
        assertEquals(1, actualMessageRequest.getId().intValue());
        assertSame(fromResult, actualMessageRequest.getReadDate());
        assertSame(fromResult1, actualMessageRequest.getSentDate());
        assertEquals("Text", actualMessageRequest.getText());
        assertSame(randomUUIDResult, actualMessageRequest.getUserId());
    }

    /**
     * Method under test: {@link MessageRequest#MessageRequest(Message)}
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

        Message message = new Message();
        message.setId(UUID.randomUUID());
        message.setRead(true);
        message.setReadDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        Date fromResult = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        message.setSentDate(fromResult);
        message.setText("Text");
        message.setUser(user);
        MessageRequest actualMessageRequest = new MessageRequest(message);
        assertSame(randomUUIDResult, actualMessageRequest.getUserId());
        assertEquals("Text", actualMessageRequest.getText());
        assertSame(fromResult, actualMessageRequest.getSentDate());
    }
}

