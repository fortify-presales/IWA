package com.microfocus.example.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {JwtUtils.class})
@ExtendWith(SpringExtension.class)
class JwtUtilsTest {
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Method under test: {@link JwtUtils#generateJwtToken(Authentication)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGenerateJwtToken() {
        // TODO: Complete this test.
        //   Reason: R033 Missing Spring properties.
        //   Failed to create Spring context due to unresolvable @Value
        //   properties: Spring @Value annotation can't be resolved: field 'jwtExpirationMs'
        //   Please check that at least one of the property files is provided
        //   and contains required variables:
        //   - application-test.properties (file missing)
        //   See https://diff.blue/R033 to resolve this issue.

        // Arrange
        // TODO: Populate arranged inputs
        Authentication authentication = null;

        // Act
        String actualGenerateJwtTokenResult = this.jwtUtils.generateJwtToken(authentication);

        // Assert
        // TODO: Add assertions on result
    }

    /**
     * Method under test: {@link JwtUtils#generateAndSetSession(HttpServletRequest, HttpServletResponse, Authentication)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGenerateAndSetSession() {
        // TODO: Complete this test.
        //   Reason: R033 Missing Spring properties.
        //   Failed to create Spring context due to unresolvable @Value
        //   properties: Spring @Value annotation can't be resolved: field 'jwtExpirationMs'
        //   Please check that at least one of the property files is provided
        //   and contains required variables:
        //   - application-test.properties (file missing)
        //   See https://diff.blue/R033 to resolve this issue.

        // Arrange
        // TODO: Populate arranged inputs
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        Authentication authentication = null;

        // Act
        String actualGenerateAndSetSessionResult = this.jwtUtils.generateAndSetSession(request, response, authentication);

        // Assert
        // TODO: Add assertions on result
    }
}

