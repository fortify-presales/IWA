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

package com.microfocus.example.service;

import com.microfocus.example.entity.SMS;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsSenderService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final String SERVICE_NAME = getClass().getName();

    @Value("${app.twilio.phone-number}")
    private String twilioFrom;

    @Value("${app.twilio.sid}")
    private String twilioSid;

    @Value("${app.twilio.auth-token}")
    private String twilioAuthToken;

    public String sendSms(SMS sms) {
        log.debug("Sending SMS to: " + sms.getTo());

        String smsFrom = System.getenv("TWILIO_PHONE_NUMBER");
        if (sms.getFrom() != null && !sms.getFrom().isEmpty())
            smsFrom = sms.getFrom();
        if (smsFrom == null || smsFrom.isEmpty()) smsFrom = twilioFrom;
        String smsUsername = System.getenv("TWILIO_ACCOUNT_SID");
        if (smsUsername == null || smsUsername.isEmpty()) smsUsername = twilioSid;
        String smsPassword = System.getenv("TWILIO_AUTH_TOKEN");
        if (smsPassword == null || smsPassword.isEmpty()) smsPassword = twilioAuthToken;

        Twilio.init(smsUsername, smsPassword);

        Message message = Message.creator(new PhoneNumber(sms.getTo()),
                new PhoneNumber(smsFrom), sms.getMessage()).create();

        return message.getSid();
    }


}
