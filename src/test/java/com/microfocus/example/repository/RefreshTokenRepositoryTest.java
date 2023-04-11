package com.microfocus.example.repository;

import com.microfocus.example.BaseIntegrationTest;
import com.microfocus.example.DataSeeder;
import com.microfocus.example.entity.RefreshToken;
import com.microfocus.example.entity.User;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RefreshTokenRepositoryTest extends BaseIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Test
    public void a_refreshTokenRepository_existsById() {
        if (!refreshTokenRepository.existsById(DataSeeder.TEST_REFRESH_TOKEN1_ID)) fail("Test Refresh Token 1 does not exist");
    }

    @Test
    public void b_refreshTokenRepository_findById() {
        Optional<RefreshToken> rt = refreshTokenRepository.findById(DataSeeder.TEST_REFRESH_TOKEN1_ID);
        if (rt.isPresent())
            assertThat(rt.get().getUser().getId()).isEqualTo(DataSeeder.TEST_REFRESH_TOKEN1_USERID);
        else
            fail("Test Refresh Token 1 not found");
    }
/*
    @Test
    public void c_refreshTokenRepository_findByUserId() {
        List<RefreshToken> rts = refreshTokenRepository.findByUserId(DataSeeder.TEST_REFRESH_TOKEN1_ID);
        assertThat(rts.size()).isEqualTo(1);
    }
*/
    @Test
    public void d_refreshTokenRepository_persist() {
        RefreshToken rt = DataSeeder.generateRefreshToken();
        Optional<User> u = userRepository.findById(DataSeeder.TEST_REFRESH_TOKEN2_USERID);
        if (u.isPresent()) {
            rt.setUser(u.get());
        } else {
            fail("Unable to retrieve user using id: " + DataSeeder.TEST_REFRESH_TOKEN2_USERID);
        }
        rt = refreshTokenRepository.saveAndFlush(rt);
        Optional<RefreshToken> optRt = refreshTokenRepository.findById(rt.getId());
        if (optRt.isPresent()) {
            assertThat(optRt.get().getExpiryDate().equals(DataSeeder.TEST_REFRESH_TOKEN2_EXPIRY_DATE));
        } else {
            fail("Test Refresh Message 2 not found");
        }
    }

    @Test
    public void e_refreshTokenRepository_update() {
        Optional<RefreshToken> optRt = refreshTokenRepository.findById(DataSeeder.TEST_REFRESH_TOKEN1_ID);
        if (optRt.isPresent()) {
            RefreshToken rt = optRt.get();
            rt.setExpiryDate(DataSeeder.TEST_REFRESH_TOKEN1_EXPIRY_DATE.plusMillis(100000));
            refreshTokenRepository.saveAndFlush(rt);
            Optional<RefreshToken> optRt2 = refreshTokenRepository.findById(DataSeeder.TEST_REFRESH_TOKEN1_ID);
            if (optRt.isPresent()) {
                RefreshToken optRt3 = optRt.get();
                assertThat(optRt3.getUser().getId()).isEqualTo(DataSeeder.TEST_REFRESH_TOKEN1_USERID);
                assertThat(optRt3.getExpiryDate()).isEqualTo(DataSeeder.TEST_REFRESH_TOKEN1_EXPIRY_DATE.plusMillis(100000));
            } else
                fail("Test Refresh Token 1 not found");
        } else
            fail("Test Refresh Token 1 not found");
    }

}
