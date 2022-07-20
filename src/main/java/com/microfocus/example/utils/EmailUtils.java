/*
        Insecure Web App (IWA)

        Copyright (C) 2020-2022 Micro Focus or one of its affiliates

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

import com.microfocus.example.payload.request.EmailRequest;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailUtils {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${email.server}")
    private String emailServer;

    private static String EMAIL_SERVER;

    @Value("${email.server}")
    public void setEmailServer(String emailServer) {
        log.debug("setting EMAIL_SERVER to: " + emailServer);
        EmailUtils.EMAIL_SERVER = emailServer;
    }

    @Value("${email.port}")
    private int emailPort;

    private static int EMAIL_PORT;

    @Value("${email.port}")
    public void setEmailPort(int emailPort) {
        EmailUtils.EMAIL_PORT = emailPort;
    }

    @Value("${email.username}")
    private String emailUsername;

    private static String EMAIL_USERNAME;

    @Value("${email.username}")
    public void setEmailUsername(String emailUsername) {
        EmailUtils.EMAIL_USERNAME = emailUsername;
    }

    @Value("${email.password}")
    private String emailPassword;

    private static String EMAIL_PASSWORD;

    @Value("${email.password}")
    public void setEmailPassword(String emailPassword) {
        EmailUtils.EMAIL_PASSWORD = emailPassword;
    }

    public static void sendEmail(EmailRequest request) throws Exception {

        String ENDL = System.getProperty("line.separator");

        Email server = new SimpleEmail();
        server.setHostName(EMAIL_SERVER);
        server.setSmtpPort(EMAIL_PORT);
        server.setAuthenticator(new DefaultAuthenticator(EMAIL_USERNAME, EMAIL_PASSWORD));
        server.setSSLOnConnect(true);
        if (request.getDebug()) {
            server.setDebug(true);
        }
        server.addTo(request.getTo());
        server.setFrom(request.getFrom());
        server.setSubject(request.getSubject());
        server.setMsg(request.getBody());
        server.setBounceAddress(request.getBounce());
        server.send();
    }
}
