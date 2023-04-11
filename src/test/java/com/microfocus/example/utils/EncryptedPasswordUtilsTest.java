package com.microfocus.example.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class EncryptedPasswordUtilsTest {
    /**
     * Method under test: {@link EncryptedPasswordUtils#encryptPassword(String)}
     */
    @Test
    void testEncryptPassword() {
        assertEquals("ù\u001fÓ¸\u000f*èÃÐ4\"�ÛÖT", EncryptedPasswordUtils.encryptPassword("iloveyou"));
    }

    /**
     * Method under test: {@link EncryptedPasswordUtils#matches(String, String)}
     */
    @Test
    void testMatches() {
        assertFalse(EncryptedPasswordUtils.matches("Password1", "Password2"));
    }
}

