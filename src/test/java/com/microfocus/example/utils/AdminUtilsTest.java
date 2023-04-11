package com.microfocus.example.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AdminUtilsTest {
    /**
     * Method under test: {@link AdminUtils#getDbStatus(int)}
     */
    @Test
    void testGetDbStatus() {
        assertEquals("READY", AdminUtils.getDbStatus(1));
    }

    /**
     * Method under test: {@link AdminUtils#getBackupId()}
     */
    @Test
    void testGetBackupId() {
        assertEquals(1553932502, AdminUtils.getBackupId());
    }
}

