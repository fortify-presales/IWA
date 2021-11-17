/*
        Insecure Web App (IWA)

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

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class UserUtils {

    private static final Logger log = LoggerFactory.getLogger(AdminUtils.class);

    public static final String USER_INFO_FILE = "user_info.json";
    public static final String NEWSLETTER_USER_FILE = "newsletter_registration.json";
    public static final String DEFAULT_ROLE = "guest";

    public static void writeUser(String username, String password) throws IOException {
        JsonFactory jsonFactory = new JsonFactory();

        File dataFile = new File(getFilePath(USER_INFO_FILE));
        if (dataFile.createNewFile()){
            log.debug("Created: " + getFilePath(USER_INFO_FILE));
        }

        JsonGenerator jGenerator = jsonFactory.createGenerator(dataFile, JsonEncoding.UTF8);

        jGenerator.writeStartObject();

        jGenerator.writeFieldName("username");
        jGenerator.writeRawValue("\"" + username + "\"");

        jGenerator.writeFieldName("password");
        jGenerator.writeRawValue("\"" + password + "\"");

        jGenerator.writeFieldName("role");
        jGenerator.writeRawValue("\"default\"");

        jGenerator.writeEndObject();

        jGenerator.close();
    }

    public static void registerUser(String firstName, String lastName, String email) throws IOException, ParseException {
        JsonFactory jsonFactory = new JsonFactory();

        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = new JSONArray();

        File dataFile = new File(getFilePath(NEWSLETTER_USER_FILE));
        if (dataFile.exists()) {
            jsonArray = (JSONArray) jsonParser.parse(new FileReader(getFilePath(NEWSLETTER_USER_FILE)));
        } else {
            dataFile.createNewFile();
            log.debug("Created: " + getFilePath(NEWSLETTER_USER_FILE));
        }

        try (OutputStream fos = new FileOutputStream(dataFile, false)) {

            JsonGenerator jGenerator = jsonFactory.createGenerator(fos, JsonEncoding.UTF8);
            jGenerator.writeStartArray();

            for (Object jsonObject : jsonArray)
            {
                jGenerator.writeStartObject();
                JSONObject person = (JSONObject) jsonObject;
                jGenerator.writeFieldName("firstName");
                jGenerator.writeRawValue("\"" + (String) person.get("firstName") + "\"");
                jGenerator.writeFieldName("lastName");
                jGenerator.writeRawValue("\"" + (String) person.get("lastName") + "\"");
                jGenerator.writeFieldName("email");
                jGenerator.writeRawValue("\"" + (String) person.get("email") + "\"");
                jGenerator.writeFieldName("role");
                jGenerator.writeRawValue("\"" + (String) person.get("role") + "\"");
                jGenerator.writeEndObject();

            }

            // write new user
            jGenerator.writeStartObject();
            jGenerator.writeFieldName("firstName");
            jGenerator.writeRawValue("\"" + firstName + "\"");
            jGenerator.writeFieldName("lastName");
            jGenerator.writeRawValue("\"" + lastName + "\"");
            jGenerator.writeFieldName("email");
            jGenerator.writeRawValue("\"" + email + "\"");
            jGenerator.writeFieldName("role");
            jGenerator.writeRawValue("\"" + DEFAULT_ROLE + "\"");
            jGenerator.writeEndObject();

            jGenerator.writeEndArray();

            jGenerator.close();
        }

    }

    public void logZipContents(String fName)
            throws IOException, SecurityException, IllegalStateException, NoSuchElementException {
        ZipFile zf = new ZipFile(fName);
        @SuppressWarnings("unchecked")
		Enumeration<ZipEntry> e = (Enumeration<ZipEntry>) zf.entries();
        while (e.hasMoreElements()) {
            log.info(e.nextElement().toString());
        }
    }

    private static String getFilePath(String relativePath) {
        return System.getProperty("user.home") + File.separatorChar + relativePath;
    }

}
