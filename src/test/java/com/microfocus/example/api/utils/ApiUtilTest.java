package com.microfocus.example.api.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.connector.Request;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.server.ResponseStatusException;

class ApiUtilTest {
    /**
     * Method under test: {@link ApiUtil#setExampleResponse(NativeWebRequest, String, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testSetExampleResponse() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "javax.servlet.http.HttpServletResponse.addHeader(String, String)" because the return value of "org.springframework.web.context.request.NativeWebRequest.getNativeResponse(java.lang.Class)" is null
        //       at com.microfocus.example.api.utils.ApiUtil.setExampleResponse(ApiUtil.java:34)
        //   See https://diff.blue/R013 to resolve this issue.

        ApiUtil.setExampleResponse(new ServletWebRequest(new MockHttpServletRequest()), "text/plain", "Example");
    }

    /**
     * Method under test: {@link ApiUtil#setExampleResponse(NativeWebRequest, String, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testSetExampleResponse2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "org.springframework.web.context.request.NativeWebRequest.getNativeResponse(java.lang.Class)" because "req" is null
        //       at com.microfocus.example.api.utils.ApiUtil.setExampleResponse(ApiUtil.java:34)
        //   See https://diff.blue/R013 to resolve this issue.

        ApiUtil.setExampleResponse(null, "text/plain", "Example");
    }

    /**
     * Method under test: {@link ApiUtil#checkApiKey(NativeWebRequest)}
     */
    @Test
    void testCheckApiKey() {
        assertThrows(ResponseStatusException.class,
                () -> ApiUtil.checkApiKey(new ServletWebRequest(new MockHttpServletRequest())));
    }

    /**
     * Method under test: {@link ApiUtil#checkApiKey(NativeWebRequest)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCheckApiKey2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "org.apache.coyote.Request.getHeader(String)" because "this.coyoteRequest" is null
        //       at org.apache.catalina.connector.Request.getHeader(Request.java:2249)
        //       at com.microfocus.example.api.utils.ApiUtil.checkApiKey(ApiUtil.java:43)
        //   See https://diff.blue/R013 to resolve this issue.

        ApiUtil.checkApiKey(new ServletWebRequest(new Request(new Connector())));
    }
}

