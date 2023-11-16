package com.microfocus.example.utils;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.json.simple.parser.ParseException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class UserUtilsTest {
    /**
     * Method under test: {@link UserUtils#registerUser(String, String, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testRegisterUser() throws IOException, ParseException {
        // TODO: Complete this test.
        //   Reason: R011 Sandboxing policy violation.
        //   Diffblue Cover ran code in your project that tried
        //     to access files outside the temporary directory (file 'C:\Users\User\newsletter_registration.json', permission 'write').
        //   Diffblue Cover's default sandboxing policy disallows this in order to prevent
        //   your code from damaging your system environment.
        //   See https://diff.blue/R011 to resolve this issue.

        UserUtils.registerUser("Jane", "Doe", "jane.doe@example.org");
    }

    /**
     * Method under test: {@link UserUtils#registerUser(String, String, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testRegisterUser2() throws IOException, ParseException {
        // TODO: Complete this test.
        //   Reason: R011 Sandboxing policy violation.
        //   Diffblue Cover ran code in your project that tried
        //     to access files outside the temporary directory (file 'C:\Users\User\newsletter_registration.json', permission 'write').
        //   Diffblue Cover's default sandboxing policy disallows this in order to prevent
        //   your code from damaging your system environment.
        //   See https://diff.blue/R011 to resolve this issue.

        UserUtils.registerUser("foo", "foo", "foo");
    }

    /**
     * Method under test: {@link UserUtils#logZipContents(String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testLogZipContents() throws IOException, IllegalStateException, SecurityException, NoSuchElementException {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.nio.file.NoSuchFileException: F Name
        //       at sun.nio.fs.WindowsException.translateToIOException(WindowsException.java:85)
        //       at sun.nio.fs.WindowsException.rethrowAsIOException(WindowsException.java:103)
        //       at sun.nio.fs.WindowsException.rethrowAsIOException(WindowsException.java:108)
        //       at sun.nio.fs.WindowsFileAttributeViews$Basic.readAttributes(WindowsFileAttributeViews.java:53)
        //       at sun.nio.fs.WindowsFileAttributeViews$Basic.readAttributes(WindowsFileAttributeViews.java:38)
        //       at sun.nio.fs.WindowsFileSystemProvider.readAttributes(WindowsFileSystemProvider.java:199)
        //       at java.nio.file.Files.readAttributes(Files.java:1851)
        //       at java.util.zip.ZipFile$Source.get(ZipFile.java:1264)
        //       at java.util.zip.ZipFile$CleanableResource.<init>(ZipFile.java:709)
        //       at java.util.zip.ZipFile.<init>(ZipFile.java:243)
        //       at java.util.zip.ZipFile.<init>(ZipFile.java:172)
        //       at java.util.zip.ZipFile.<init>(ZipFile.java:143)
        //       at com.microfocus.example.utils.UserUtils.logZipContents(UserUtils.java:133)
        //   See https://diff.blue/R013 to resolve this issue.

        (new UserUtils()).logZipContents("F Name");
    }
}

