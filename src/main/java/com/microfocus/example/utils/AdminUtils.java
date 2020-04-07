/*
        Simple Secure App

        Copyright (C) 2020 Micro Focus or one of its affiliates

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.microfocus.example.utils;

import com.microfocus.example.exception.BackupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

/**
 * Administration Utilities
 *
 * @author Kevin. A. Lee
 */
public class AdminUtils {

    private static final Logger log = LoggerFactory.getLogger(AdminUtils.class);

    public static int startDbBackup(String profile) throws BackupException {
        int backupId = 0;
        if (profile.matches("^.*[^a-zA-Z0-9 ].*$"))
            throw new BackupException("Profile contains non alpha numeric characters, cannot start backup");
        String[] backupCommand = {
                "cmd.exe", "/K", "dir", "c:\\util\\backup.bat",
                "-profile", profile
        };
        String[] cleanupCommand = {
                "cmd.exe", "/K", "c:\\util\\cleanup.bat"
        };
        Process p = null;
        try {
            log.info("Running: " + Arrays.toString(backupCommand));
            p = Runtime.getRuntime().exec(backupCommand);
            log.info("Exit value: " + p.exitValue());
            log.info("Running: " + Arrays.toString(cleanupCommand));
            p = Runtime.getRuntime().exec(cleanupCommand);
            log.info("Exit value: " + p.exitValue());
        } catch (IOException ignored) {
            log.error(ignored.getMessage());
        }
        backupId = getBackupId();
        return backupId;
    }

    public static int startDbRestore(int backupId) {
        int restoreId = 0;
        // Note: database restore not currently supported from website
        return restoreId;
    }

    public static String getDbStatus(int backupId) {
        if (Boolean.parseBoolean(isLocked(backupId))) {
            return "LOCKED";
        }
        return isReady(backupId);
    }

    public static int getBackupId() {
        return genId();
    }

    //
    // Private methods
    //

    private static int genId() {
        SecureRandom sr = new SecureRandom();
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException ignored) {
            log.error(ignored.getMessage());
        }
        return sr.nextInt();
    }

    private static String isLocked(int backupId) {
        return "FALSE";
    }

    private static String isReady(int backupId) {
        return "READY";
    }

}
