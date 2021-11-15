package com.microfocus.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = System.getProperty("user.home") + File.separatorChar + "upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
