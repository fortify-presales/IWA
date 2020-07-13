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

package com.microfocus.example.api.exception;

import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionTranslator {

    private final ErrorAttributes errorAttributes;

    public ExceptionTranslator(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> processConstraintViolationException(WebRequest request) {
        request.setAttribute("javax.servlet.error.status_code", HttpStatus.BAD_REQUEST.value(), RequestAttributes.SCOPE_REQUEST);
        return errorAttributes.getErrorAttributes(request, false);
    }
}
