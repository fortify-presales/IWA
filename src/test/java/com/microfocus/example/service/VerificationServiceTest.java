package com.microfocus.example.service;

import com.microfocus.example.BaseIntegrationTest;
import com.microfocus.example.DataSeeder;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VerificationServiceTest extends BaseIntegrationTest {

    @Autowired
    VerificationService verificationService;

    @Test
    public void a_verificationService_findById() {
        String userId = DataSeeder.TEST_USER1_ID.toString();
        int generatedOtp = verificationService.generateOTP(userId);
        assertThat(generatedOtp).isGreaterThan(0);
        int serverOtp = verificationService.getOtp(userId);
        assertThat(serverOtp).isEqualTo(generatedOtp);
    }

}
