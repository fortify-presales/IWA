package com.microfocus.example.payload.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EmailRequestTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link EmailRequest#EmailRequest(String, String, String, String)}
     *   <li>{@link EmailRequest#setBody(String)}
     *   <li>{@link EmailRequest#setBounce(String)}
     *   <li>{@link EmailRequest#setDebug(boolean)}
     *   <li>{@link EmailRequest#setFrom(String)}
     *   <li>{@link EmailRequest#setSubject(String)}
     *   <li>{@link EmailRequest#setTo(String)}
     *   <li>{@link EmailRequest#toString()}
     *   <li>{@link EmailRequest#getBody()}
     *   <li>{@link EmailRequest#getBounce()}
     *   <li>{@link EmailRequest#getDebug()}
     *   <li>{@link EmailRequest#getFrom()}
     *   <li>{@link EmailRequest#getSubject()}
     *   <li>{@link EmailRequest#getTo()}
     * </ul>
     */
    @Test
    void testConstructor() {
        EmailRequest actualEmailRequest = new EmailRequest("jane.doe@example.org", "alice.liddell@example.org",
                "Hello from the Dreaming Spires", "Not all who wander are lost");
        actualEmailRequest.setBody("Not all who wander are lost");
        actualEmailRequest.setBounce("Bounce");
        actualEmailRequest.setDebug(true);
        actualEmailRequest.setFrom("jane.doe@example.org");
        actualEmailRequest.setSubject("Hello from the Dreaming Spires");
        actualEmailRequest.setTo("alice.liddell@example.org");
        String actualToStringResult = actualEmailRequest.toString();
        assertEquals("Not all who wander are lost", actualEmailRequest.getBody());
        assertEquals("Bounce", actualEmailRequest.getBounce());
        assertTrue(actualEmailRequest.getDebug());
        assertEquals("jane.doe@example.org", actualEmailRequest.getFrom());
        assertEquals("Hello from the Dreaming Spires", actualEmailRequest.getSubject());
        assertEquals("alice.liddell@example.org", actualEmailRequest.getTo());
        assertEquals("EmailRequest{from='jane.doe@example.org', to='alice.liddell@example.org', subject='Hello from the"
                + " Dreaming Spires'}", actualToStringResult);
    }
}

