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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Random;

/**
 * Administration Utilities
 *
 * @author Kevin. A. Lee
 */
public class AdminUtils {

    private static final Logger log = LoggerFactory.getLogger(AdminUtils.class);

    public static int startDbBackup(String profile) {
        int backupId = 0;
// INSECURE EXAMPLE: Command Injection       
        String cmd = "cmd.exe /K \"c:\\util\\backup.bat -profile " + profile
                + "&& c:\\util\\cleanup.bat\"";
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ignored) {
            log.error(ignored.getMessage());
        }
// END EXAMPLE         
        backupId = getBackupId();
        log.info("Backup id: " + backupId);
        return backupId;
    }

    public static int startDbRestore(int backupId) {
        int restoreId = 0;
        // Note: database restore not currently supported from website
        return restoreId;
    }

    public static String getDbStatus(int backupId) {
// INSECURE EXAMPLE: Often Misused: Boolean.getBoolean()     	
        if (Boolean.getBoolean(isLocked(backupId))) {
            return "LOCKED";
        }
// END EXAMPLE        
        return isReady(backupId);
    }

    public static int getBackupId() {
        return genId();
    }

    //
    // Private methods
    //

// INSECURE EXAMPLE: Insecure Randomness and Hardcoded Seed
    private static int genId() {
        Random r = new Random();
        r.setSeed(12345);
        return r.nextInt();
    }
// END EXAMPLE

    private static String isLocked(int backupId) {
        return "FALSE";
    }

    private static String isReady(int backupId) {
        return "READY";
    }

    /*
            // Path Manipulation
            File rFile = new File("/usr/local/apfr/reports/" + backupForm.getProfile());
            rFile.delete();

            // SPEL raw expression
            String expression = backupForm.getProfile();
            SpelExpressionParser parser = new SpelExpressionParser();
            SpelExpression spelExpr = parser.parseRaw(expression);

            // Log forging - try ->twenty-one%0a%0aINFO:+User+logged+out%3dbadguy<-
            try {
                int refreshInterval = Integer.parseInt(backupForm.getRefreshInterval());
                log.info("Backup refresh interval: " + refreshInterval);
            } catch (NumberFormatException nfe) {
                log.info("Failed to parse refreshInterval = " + backupForm.getRefreshInterval());
            }
     */

}
