/*
        Secure Web App

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microfocus.example.config.SecurityConfiguration;
import com.microfocus.example.entity.ApiErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class BasicAuthenticationEntryPointCustom extends BasicAuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(BasicAuthenticationEntryPointCustom.class);

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
            throws IOException, ServletException {
        response.addHeader("WWW-Authenticate", "Basic realm='" + getRealmName() + "'");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ApiErrorResponse apiResponse = new ApiErrorResponse
                .ApiErrorResponseBuilder()
                    .withStatus(HttpStatus.UNAUTHORIZED)
                    .atTime(LocalDateTime.now(ZoneOffset.UTC))
                    .withMessage("Not authorised")
                    .withDetails(authEx.getMessage())
                    .build();
        ResponseEntity<ApiErrorResponse> apiError = new ResponseEntity<ApiErrorResponse>(apiResponse, HttpStatus.UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(apiError.getBody());
        PrintWriter writer = response.getWriter();
        writer.println(jsonString);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName(SecurityConfiguration.REALM_NAME);
        super.afterPropertiesSet();
    }
}
