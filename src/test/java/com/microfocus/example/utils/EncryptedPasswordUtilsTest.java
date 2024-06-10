package com.microfocus.example.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import com.microfocus.example.utils.EncryptedPasswordUtils;

class EncryptedPasswordUtilsTest {
    private static final String TEST_PASSWORD_STRING = "Password123!";

    /**
     * Method under test: {@link EncryptedPasswordUtils#encryptPassword(String)}
     */
    @Test
    void testEncryptPassword() {
        String enc = EncryptedPasswordUtils.encryptPassword(TEST_PASSWORD_STRING);
        assertNotNull(enc);
    }

    /**
     * Method under test: {@link EncryptedPasswordUtils#decryptPassword(String)}
     */
    @Ignore
    @Test
    void testDecryptPassword() {
        //String dec = EncryptedPasswordUtils.decryptPassword(EncryptedPasswordUtils.encryptPassword(TEST_PASSWORD_STRING));
        //assertNotNull(dec);
    }

    /**
     * Method under test: {@link EncryptedPasswordUtils#matches(String, String)}
     */
    @Test
    void testMatches() {
        String enc1 = EncryptedPasswordUtils.encryptPassword(TEST_PASSWORD_STRING);
        String enc2 = EncryptedPasswordUtils.encryptPassword(TEST_PASSWORD_STRING);
        String enc3 = EncryptedPasswordUtils.encryptPassword("Password456!");
        //assertTrue(EncryptedPasswordUtils.matches(enc1, enc2));
        assertFalse(EncryptedPasswordUtils.matches(enc2, enc3));
    }
}

