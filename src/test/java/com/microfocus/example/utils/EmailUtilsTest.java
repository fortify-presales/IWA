package com.microfocus.example.utils;

import org.junit.jupiter.api.Test;

class EmailUtilsTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link EmailUtils}
     *   <li>{@link EmailUtils#setEmailPassword(String)}
     *   <li>{@link EmailUtils#setEmailPort(int)}
     *   <li>{@link EmailUtils#setEmailUsername(String)}
     * </ul>
     */
    @Test
    void testConstructor() {
        // TODO: Complete this test.
        //   Reason: R002 Missing observers.
        //   Diffblue Cover was unable to create an assertion.
        //   Add getters for the following fields or make them package-private:
        //     EmailUtils.EMAIL_PASSWORD
        //     EmailUtils.EMAIL_PORT
        //     EmailUtils.EMAIL_SERVER
        //     EmailUtils.EMAIL_USERNAME
        //     EmailUtils.emailPassword
        //     EmailUtils.emailPort
        //     EmailUtils.emailServer
        //     EmailUtils.emailUsername
        //     EmailUtils.log

        EmailUtils actualEmailUtils = new EmailUtils();
        actualEmailUtils.setEmailPassword("iloveyou");
        actualEmailUtils.setEmailPort(8080);
        actualEmailUtils.setEmailUsername("janedoe");
    }
}

