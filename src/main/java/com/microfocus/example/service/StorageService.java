package com.microfocus.example.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file);
    void store(Path path, String dstFileName);

    Stream<Path> loadAll();
    Stream<Path> loadAll(List<String> mimeTypeList);

    Path load(String filename);

    Resource loadAsResource(String filename);
    
    Resource loadAsResource(String filename, boolean traverse);

    void deleteAll();

}
