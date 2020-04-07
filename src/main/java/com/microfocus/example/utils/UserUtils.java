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
        ZipFile zf = new ZipFile(fName);
        Enumeration<ZipEntry> e = (Enumeration<ZipEntry>) zf.entries();
        while (e.hasMoreElements()) {
            log.info(e.nextElement().toString());
        }
        zf.close();
    }

}
