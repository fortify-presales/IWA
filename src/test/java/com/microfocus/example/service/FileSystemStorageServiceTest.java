package com.microfocus.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.microfocus.example.config.StorageProperties;
import com.microfocus.example.exception.StorageException;
import com.microfocus.example.exception.StorageFileNotFoundException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {FileSystemStorageService.class, StorageProperties.class})
@ExtendWith(SpringExtension.class)
class FileSystemStorageServiceTest {
    @Autowired
    private FileSystemStorageService fileSystemStorageService;

    @Autowired
    private StorageProperties storageProperties;

    /**
     * Method under test: {@link FileSystemStorageService#store(Path, String)}
     */
    @Test
    void testStore() {
        assertThrows(StorageException.class,
                () -> fileSystemStorageService.store(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt"), "foo.txt"));
        assertThrows(StorageException.class,
                () -> fileSystemStorageService.store(Paths.get(System.getProperty("java.io.tmpdir"), ""), "foo.txt"));
        assertThrows(StorageException.class, () -> fileSystemStorageService.store(null, "foo.txt"));
        assertThrows(StorageException.class, () -> fileSystemStorageService
                .store(new MockMultipartFile("Name", new ByteArrayInputStream("AAAAAAAA".getBytes("UTF-8")))));
        assertThrows(StorageException.class, () -> fileSystemStorageService
                .store(new MockMultipartFile("Name", new ByteArrayInputStream(new byte[]{}))));
    }

    /**
     * Method under test: {@link FileSystemStorageService#loadAll()}
     */
    @Test
    void testLoadAll() {
        // TODO: Complete this test.
        //   Reason: R002 Missing observers.
        //   Diffblue Cover was unable to create an assertion.
        //   Add getters for the following fields or make them package-private:
        //     AbstractPipeline.combinedFlags
        //     AbstractPipeline.depth
        //     AbstractPipeline.linkedOrConsumed
        //     AbstractPipeline.nextStage
        //     AbstractPipeline.parallel
        //     AbstractPipeline.previousStage
        //     AbstractPipeline.sourceAnyStateful
        //     AbstractPipeline.sourceCloseAction
        //     AbstractPipeline.sourceOrOpFlags
        //     AbstractPipeline.sourceSpliterator
        //     AbstractPipeline.sourceStage
        //     AbstractPipeline.sourceSupplier
        //     3.this$0
        //     3.val$mapper
        //     FileSystemStorageService.rootLocation

        fileSystemStorageService.loadAll();
    }

    /**
     * Method under test: {@link FileSystemStorageService#loadAll(List)}
     */
    @Test
    void testLoadAll2() {
        // TODO: Complete this test.
        //   Reason: R002 Missing observers.
        //   Diffblue Cover was unable to create an assertion.
        //   Add getters for the following fields or make them package-private:
        //     AbstractPipeline.combinedFlags
        //     AbstractPipeline.depth
        //     AbstractPipeline.linkedOrConsumed
        //     AbstractPipeline.nextStage
        //     AbstractPipeline.parallel
        //     AbstractPipeline.previousStage
        //     AbstractPipeline.sourceAnyStateful
        //     AbstractPipeline.sourceCloseAction
        //     AbstractPipeline.sourceOrOpFlags
        //     AbstractPipeline.sourceSpliterator
        //     AbstractPipeline.sourceStage
        //     AbstractPipeline.sourceSupplier
        //     3.this$0
        //     3.val$mapper
        //     FileSystemStorageService.rootLocation

        fileSystemStorageService.loadAll(new ArrayList<>());
    }

    /**
     * Method under test: {@link FileSystemStorageService#load(String)}
     */
    @Test
    void testLoad() {
        // TODO: Complete this test.
        //   Reason: R002 Missing observers.
        //   Diffblue Cover was unable to create an assertion.
        //   Add getters for the following fields or make them package-private:
        //     FileSystemStorageService.rootLocation

        fileSystemStorageService.load("foo.txt");
    }

    /**
     * Method under test: {@link FileSystemStorageService#loadAsResource(String)}
     */
    @Test
    void testLoadAsResource() {
        assertThrows(StorageFileNotFoundException.class, () -> fileSystemStorageService.loadAsResource("foo.txt"));
        assertThrows(StorageFileNotFoundException.class,
                () -> fileSystemStorageService.loadAsResource("foo.txt", true));
        assertThrows(StorageFileNotFoundException.class,
                () -> fileSystemStorageService.loadAsResource("foo.txt", false));
    }

    /**
     * Method under test: {@link FileSystemStorageService#loadAsResource(String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testLoadAsResource2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.nio.file.InvalidPathException: Illegal char <:> at index 19: Could not read file:
        //       at sun.nio.fs.WindowsPathParser.normalize(WindowsPathParser.java:182)
        //       at sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:153)
        //       at sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:77)
        //       at sun.nio.fs.WindowsPath.parse(WindowsPath.java:92)
        //       at sun.nio.fs.WindowsFileSystem.getPath(WindowsFileSystem.java:232)
        //       at java.nio.file.Path.resolve(Path.java:515)
        //       at com.microfocus.example.service.FileSystemStorageService.load(FileSystemStorageService.java:144)
        //       at com.microfocus.example.service.FileSystemStorageService.loadAsResource(FileSystemStorageService.java:162)
        //       at com.microfocus.example.service.FileSystemStorageService.loadAsResource(FileSystemStorageService.java:149)
        //   See https://diff.blue/R013 to resolve this issue.

        fileSystemStorageService.loadAsResource("Could not read file: ");
    }

    /**
     * Method under test: {@link FileSystemStorageService#loadAsResource(String)}
     */
    @Test
    void testLoadAsResource3() {
        Resource actualLoadAsResourceResult = fileSystemStorageService.loadAsResource("");
        assertEquals("URL [file:/C:/Users/erick/upload-dir/]", actualLoadAsResourceResult.getDescription());
        assertTrue(actualLoadAsResourceResult.isFile());
        assertEquals("", actualLoadAsResourceResult.getFilename());
    }

    /**
     * Method under test: {@link FileSystemStorageService#loadAsResource(String, boolean)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testLoadAsResource4() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.nio.file.InvalidPathException: Illegal char <:> at index 19: Could not read file:
        //       at sun.nio.fs.WindowsPathParser.normalize(WindowsPathParser.java:182)
        //       at sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:153)
        //       at sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:77)
        //       at sun.nio.fs.WindowsPath.parse(WindowsPath.java:92)
        //       at sun.nio.fs.WindowsFileSystem.getPath(WindowsFileSystem.java:232)
        //       at java.nio.file.Path.of(Path.java:147)
        //       at java.nio.file.Paths.get(Paths.java:69)
        //       at com.microfocus.example.service.FileSystemStorageService.loadAsResource(FileSystemStorageService.java:160)
        //   See https://diff.blue/R013 to resolve this issue.

        fileSystemStorageService.loadAsResource("Could not read file: ", true);
    }

    /**
     * Method under test: {@link FileSystemStorageService#loadAsResource(String, boolean)}
     */
    @Test
    void testLoadAsResource5() {
        Resource actualLoadAsResourceResult = fileSystemStorageService.loadAsResource("", true);
        assertEquals("URL [file:/C:/Engenharia%20de%20Software/IWAPharmacyDirect/]",
                actualLoadAsResourceResult.getDescription());
        assertTrue(actualLoadAsResourceResult.isFile());
        assertEquals("", actualLoadAsResourceResult.getFilename());
    }

    /**
     * Method under test: {@link FileSystemStorageService#loadAsResource(String, boolean)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testLoadAsResource6() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.nio.file.InvalidPathException: Illegal char <:> at index 19: Could not read file:
        //       at sun.nio.fs.WindowsPathParser.normalize(WindowsPathParser.java:182)
        //       at sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:153)
        //       at sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:77)
        //       at sun.nio.fs.WindowsPath.parse(WindowsPath.java:92)
        //       at sun.nio.fs.WindowsFileSystem.getPath(WindowsFileSystem.java:232)
        //       at java.nio.file.Path.resolve(Path.java:515)
        //       at com.microfocus.example.service.FileSystemStorageService.load(FileSystemStorageService.java:144)
        //       at com.microfocus.example.service.FileSystemStorageService.loadAsResource(FileSystemStorageService.java:162)
        //   See https://diff.blue/R013 to resolve this issue.

        fileSystemStorageService.loadAsResource("Could not read file: ", false);
    }
}

