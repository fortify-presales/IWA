/*
        Insecure Web App (IWA)

        Copyright (C) 2021 Micro Focus or one of its affiliates

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

package com.microfocus.example.api.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

public class ApiUtil {

    public static void setExampleResponse(NativeWebRequest req, String contentType, String example) {
        try {
            req.getNativeResponse(HttpServletResponse.class).addHeader("Content-Type", contentType);
            req.getNativeResponse(HttpServletResponse.class).getOutputStream().print(example);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkApiKey(NativeWebRequest req) {
        if (!"1".equals(System.getenv("DISABLE_API_KEY")) && !"special-key".equals(req.getHeader("api_key"))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing API key!");
        }
    }
}
