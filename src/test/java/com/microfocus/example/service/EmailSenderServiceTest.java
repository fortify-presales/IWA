package com.microfocus.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import com.microfocus.example.BaseIntegrationTest;
import com.microfocus.example.DataSeeder;
import com.microfocus.example.entity.Mail;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmailSenderServiceTest extends BaseIntegrationTest {

    @Autowired
    EmailSenderService emailSenderService;

    @Value("${app.mail.from-name}")
    private String emailFromName;

    @Value("${app.mail.from-address}")
    private String emailFromAddress;

    @Test
    public void a_emailSenderService_sendMail() {
        Mail testMail = new Mail();
        testMail.setMailTo(DataSeeder.TEST_USER1_EMAIL);
        testMail.setFrom(emailFromAddress);
        testMail.setReplyTo("no-reply@iwa.onfortify.com");
        testMail.setSubject("Test mail");

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("to", DataSeeder.TEST_USER1_FIRST_NAME + " " + DataSeeder.TEST_USER1_LAST_NAME);
        model.put("message", "This is an example <b>mail</b> from <em>IWAPharmacyDirect</em>");
        model.put("from", emailFromAddress);
        testMail.setProps(model);

        // ignoring because we don't have test email server
/*
        try {
            emailSenderService.sendEmail(testMail, "email/default");
        } catch (Exception ex) {
            fail(ex.toString());
        }
*/
    }


}
