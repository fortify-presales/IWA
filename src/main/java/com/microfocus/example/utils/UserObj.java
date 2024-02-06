package com.microfocus.example.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class UserObj implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(UserObj.class);

    public String username;
    public String password;

    public UserObj(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        checkStream(in);
        in.defaultReadObject();
        log.debug("Username: {}, Password: {}", username, password);
    }

    public void checkStream(ObjectInputStream in) {
        // TBD
    }

}
