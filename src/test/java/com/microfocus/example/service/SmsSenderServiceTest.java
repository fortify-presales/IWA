package com.microfocus.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import com.microfocus.example.BaseIntegrationTest;
import com.microfocus.example.DataSeeder;
import com.microfocus.example.entity.SMS;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SmsSenderServiceTest extends BaseIntegrationTest {

    @Autowired
    SmsSenderService smsSenderService;

    @Test
    public void a_smsSenderService_sendSms() {
        SMS testSms = new SMS();
        testSms.setTo(DataSeeder.TEST_USER1_PHONE);
        testSms.setMessage("Test SMS");

        // ignoring because we don't have test sms server
/*
        try {
            String sid = smsSenderService.sendSms(testSms);
            assertThat(sid).isNotEmpty();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            fail(ex.getMessage());
        }
 */
    }


}
