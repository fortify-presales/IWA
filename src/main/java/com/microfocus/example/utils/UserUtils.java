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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UserUtils {

    private static final Logger log = LoggerFactory.getLogger(AdminUtils.class);

    public void writeUser(String username, String password) throws IOException {
        JsonFactory jfactory = new JsonFactory();

        JsonGenerator jGenerator = jfactory.createGenerator(new File("~/user_info.json"), JsonEncoding.UTF8);

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

    public void logZipContents(String fName)
            throws IOException, SecurityException, IllegalStateException, NoSuchElementException {
// INSECURE EXAMPLE: Unreleased Resource Files 	
        ZipFile zf = new ZipFile(fName);
        @SuppressWarnings("unchecked")
		Enumeration<ZipEntry> e = (Enumeration<ZipEntry>) zf.entries();
        while (e.hasMoreElements()) {
            log.info(e.nextElement().toString());
        }
        //zf.close();
// END EXAMPLE

    }

}
