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

package com.microfocus.example.config.handlers;

import com.microfocus.example.entity.CustomUserDetails;
import com.microfocus.example.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Custom Url Authentication Success Handler
 * @author Kevin A. Lee
 */
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    private static final String VERIFICATION_URL = "/verify";
    private static final String USER_HOME_URL = "/user";
    private static final String ADMIN_HOME_URL = "/admin";
    private static final String INDEX_URL = "/";

    @Autowired
    private JwtUtils jwtUtils;

    private static RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException {

        HttpSession session = request.getSession(false);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String mobile = customUserDetails.getMobile();
        boolean isAdmin = customUserDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            log.debug("User is ADMIN, bypassing verification");
            bypassVerification(request, response, authentication);
        } else if (mobile.isEmpty() || !requestAndRegisterVerification(mobile)) {
            log.debug("No mobile phone provided, bypassing verification");
            bypassVerification(request, response, authentication);
        } else if (!mobile.isEmpty() && requestAndRegisterVerification(mobile)) {
            log.debug("Using users mobile number for verification: " + mobile);
            session.setAttribute("mobileDigits",
                    mobile.length() > 2 ? mobile.substring(mobile.length() - 2) : mobile);
            redirectStrategy.sendRedirect(request, response, VERIFICATION_URL);
        } else {
            String targetUrl = getTargetUrl(request, response, authentication);
            log.debug("Redirecting to: " + targetUrl);
            redirectStrategy.sendRedirect(request, response, targetUrl);
            clearAuthenticationAttributes(request);
        }
    }

    public static String getTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) {

        HttpSession session = request.getSession(false);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        boolean isAdmin = customUserDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isUser = !isAdmin;
        String targetUrl = INDEX_URL;

        if (isAdmin) {
            targetUrl = ADMIN_HOME_URL;
        } else {
            String loginReferer = (String) session.getAttribute("loginReferer");
            if (loginReferer == null || loginReferer.isEmpty()) {
                log.debug("No loginReferer; redirecting to users home page");
                targetUrl = USER_HOME_URL;
            } else {
                targetUrl = loginReferer;
                String targetPath = null;
                try {
                    targetPath = new URL(targetUrl).getPath();
                } catch (MalformedURLException ex) {
                    log.error(ex.getLocalizedMessage());
                }
                if (targetUrl.contains("?")) targetUrl = targetUrl.substring(0, targetUrl.indexOf("?"));
                if (targetPath.endsWith("/cart")) {
                    targetUrl = targetUrl.replace("/cart", "/cart/checkout");
                } else if (targetPath.endsWith("/login")) {
                    targetUrl = targetUrl.replace("/login", "/user");
                } else if (targetPath.endsWith("/register")) {
                    targetUrl = targetUrl.replace("/register", "/");
                } else if (targetPath.equals("/")) {
                    targetUrl = targetUrl + "user";
                }

            }
        }
        return targetUrl;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    private boolean requestAndRegisterVerification(String mobile) {
        /*try {
            return verificationService.requestVerification(mobile) != null;
        } catch (VerificationRequestFailedException e) {
            return false;
        }*/
        return true;
    }

    private void bypassVerification(HttpServletRequest request, HttpServletResponse response,
                                    Authentication authentication) throws IOException {
        String jwtToken = jwtUtils.generateAndSetSession(request, response, authentication);
        String targetUrl = getTargetUrl(request, response, authentication);
        log.debug("Redirecting to: " + targetUrl);
        redirectStrategy.sendRedirect(request, response, targetUrl);
        clearAuthenticationAttributes(request);
    }

    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    protected void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
}