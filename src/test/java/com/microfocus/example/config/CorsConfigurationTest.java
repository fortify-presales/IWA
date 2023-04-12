package com.microfocus.example.config;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@ContextConfiguration(classes = {CorsConfiguration.class})
@ExtendWith(SpringExtension.class)
class CorsConfigurationTest {
    @Autowired
    private CorsConfiguration corsConfiguration;

    /**
     * Method under test: {@link CorsConfiguration#addCorsMappings(CorsRegistry)}
     */
    @Test
    void testAddCorsMappings() {
        // TODO: Complete this test.
        //   Reason: R002 Missing observers.
        //   Diffblue Cover was unable to create an assertion.
        //   Add getters for the following fields or make them package-private:
        //     CorsRegistry.registrations

        corsConfiguration.addCorsMappings(new CorsRegistry());
    }

    /**
     * Method under test: {@link CorsConfiguration#addCorsMappings(CorsRegistry)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testAddCorsMappings2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "org.springframework.web.servlet.config.annotation.CorsRegistry.addMapping(String)" because "registry" is null
        //       at com.microfocus.example.config.CorsConfiguration.addCorsMappings(CorsConfiguration.java:31)
        //   See https://diff.blue/R013 to resolve this issue.

        corsConfiguration.addCorsMappings(null);
    }
}

