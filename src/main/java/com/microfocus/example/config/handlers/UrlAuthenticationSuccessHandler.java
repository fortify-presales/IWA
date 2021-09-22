/*
        Insecure Web App (IWA)

        Copyright (C) 2020 Micro Focus or one of its affiliates

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
import com.microfocus.example.entity.User;
import com.microfocus.example.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

/**
 * Custom Url Authentication Success Handler
 * @author Kevin A. Lee
 */
public class UrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;

    private static final Logger log = LoggerFactory.getLogger(UrlAuthenticationSuccessHandler.class);

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException {

        log.debug("UrlAuthenticationSuccessHandler:onAuthenticationSuccess");
        HttpSession session = request.getSession(false);

        CustomUserDetails iwaUser = (CustomUserDetails) authentication.getPrincipal();
        User user = iwaUser.getUserDetails();
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        log.debug("Generated jwtToken: " + jwtToken);
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("authorities", authentication.getAuthorities());
        session.setAttribute("jwtToken", jwtToken);

        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    protected void handle(HttpServletRequest request,
                          HttpServletResponse response, Authentication authentication)
            throws IOException {

        boolean isUser = false;
        boolean isAdmin = false;
        String targetUrl = request.getParameter("referer");
        //if (targetUrl.endsWith("/")) targetUrl = targetUrl.substring(0, targetUrl.length());
        String targetPath = new URL(targetUrl).getPath();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                isAdmin = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
                isUser = true;
            }
        }
        if (isAdmin) {
            targetUrl = "/admin";
        } else if (isUser) {
            log.debug("targetPath=" + targetPath);
            log.debug("targetUrl=" + targetUrl);
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
            // else use referring URL
        } else {
            throw new IllegalStateException();
        }

        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to "+ targetUrl);
            return;
        }

        log.debug("Redirecting to: " + targetUrl);
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
}
