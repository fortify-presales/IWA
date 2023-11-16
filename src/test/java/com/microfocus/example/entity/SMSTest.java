package com.microfocus.example.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class SMSTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link SMS#SMS()}
     *   <li>{@link SMS#setFrom(String)}
     *   <li>{@link SMS#setMessage(String)}
     *   <li>{@link SMS#setProps(Map)}
     *   <li>{@link SMS#setSubject(String)}
     *   <li>{@link SMS#setTo(String)}
     *   <li>{@link SMS#toString()}
     *   <li>{@link SMS#getFrom()}
     *   <li>{@link SMS#getMessage()}
     *   <li>{@link SMS#getProps()}
     *   <li>{@link SMS#getSubject()}
     *   <li>{@link SMS#getTo()}
     * </ul>
     */
    @Test
    void testConstructor() {
        SMS actualSms = new SMS();
        actualSms.setFrom("jane.doe@example.org");
        actualSms.setMessage("Not all who wander are lost");
        HashMap<String, Object> stringObjectMap = new HashMap<>();
        actualSms.setProps(stringObjectMap);
        actualSms.setSubject("Hello from the Dreaming Spires");
        actualSms.setTo("alice.liddell@example.org");
        String actualToStringResult = actualSms.toString();
        assertEquals("jane.doe@example.org", actualSms.getFrom());
        assertEquals("Not all who wander are lost", actualSms.getMessage());
        assertSame(stringObjectMap, actualSms.getProps());
        assertEquals("Hello from the Dreaming Spires", actualSms.getSubject());
        assertEquals("alice.liddell@example.org", actualSms.getTo());
        assertEquals("SMS{from='jane.doe@example.org', to='alice.liddell@example.org', subject='Hello from the Dreaming"
                + " Spires', message='Not all who wander are lost'}", actualToStringResult);
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link SMS#SMS(String, String, String)}
     *   <li>{@link SMS#setFrom(String)}
     *   <li>{@link SMS#setMessage(String)}
     *   <li>{@link SMS#setProps(Map)}
     *   <li>{@link SMS#setSubject(String)}
     *   <li>{@link SMS#setTo(String)}
     *   <li>{@link SMS#toString()}
     *   <li>{@link SMS#getFrom()}
     *   <li>{@link SMS#getMessage()}
     *   <li>{@link SMS#getProps()}
     *   <li>{@link SMS#getSubject()}
     *   <li>{@link SMS#getTo()}
     * </ul>
     */
    @Test
    void testConstructor2() {
        SMS actualSms = new SMS("jane.doe@example.org", "alice.liddell@example.org", "Not all who wander are lost");
        actualSms.setFrom("jane.doe@example.org");
        actualSms.setMessage("Not all who wander are lost");
        HashMap<String, Object> stringObjectMap = new HashMap<>();
        actualSms.setProps(stringObjectMap);
        actualSms.setSubject("Hello from the Dreaming Spires");
        actualSms.setTo("alice.liddell@example.org");
        String actualToStringResult = actualSms.toString();
        assertEquals("jane.doe@example.org", actualSms.getFrom());
        assertEquals("Not all who wander are lost", actualSms.getMessage());
        assertSame(stringObjectMap, actualSms.getProps());
        assertEquals("Hello from the Dreaming Spires", actualSms.getSubject());
        assertEquals("alice.liddell@example.org", actualSms.getTo());
        assertEquals("SMS{from='jane.doe@example.org', to='alice.liddell@example.org', subject='Hello from the Dreaming"
                + " Spires', message='Not all who wander are lost'}", actualToStringResult);
    }
}

