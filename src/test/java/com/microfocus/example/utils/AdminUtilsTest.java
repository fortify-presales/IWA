package com.microfocus.example.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.microfocus.example.exception.BackupException;
import org.junit.jupiter.api.Test;

class AdminUtilsTest {
    /**
     * Method under test: {@link AdminUtils#startDbBackup(String)}
     */
    @Test
    void testStartDbBackup() throws BackupException {
        assertEquals(1553932502, AdminUtils.startDbBackup("Profile"));
    }

    /**
     * Method under test: {@link AdminUtils#startDbRestore(int)}
     */
    @Test
    void testStartDbRestore() {
        assertEquals(0, AdminUtils.startDbRestore(1));
    }

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

