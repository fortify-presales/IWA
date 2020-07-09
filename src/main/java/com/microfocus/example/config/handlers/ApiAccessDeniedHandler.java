package com.microfocus.example.config.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microfocus.example.entity.ApiStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

@Component
public class ApiAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ArrayList<String> errors = new ArrayList<>();
        errors.add(ex.getLocalizedMessage());
        ApiStatusResponse apiStatusResponse = new ApiStatusResponse
                .ApiResponseBuilder()
                .withSuccess(false)
                .atTime(LocalDateTime.now(ZoneOffset.UTC))
                .withErrors(errors)
                .build();
        ResponseEntity<ApiStatusResponse> apiError = new ResponseEntity<ApiStatusResponse>(apiStatusResponse, HttpStatus.FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(apiError.getBody());
        PrintWriter writer = response.getWriter();
        writer.println(jsonString);
    }

}
