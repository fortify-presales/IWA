/*
        Java Web App

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

package com.microfocus.example.exception;

import com.microfocus.example.entity.ApiErrorResponse;
import com.microfocus.example.repository.ProductRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ProductRepositoryImpl.class);

    // Custom exception handlers

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> customerNotFound(UserNotFoundException ex, WebRequest request){
        ApiErrorResponse apiResponse = new ApiErrorResponse
                .ApiErrorResponseBuilder()
                    .withStatus(HttpStatus.NOT_FOUND)
                    .atTime(LocalDateTime.now(ZoneOffset.UTC))
                    .withMessage("User not found")
                    .withDetails("Unable to find user. Please provide a valid user id.")
                    .build();
        return new ResponseEntity<ApiErrorResponse>(apiResponse, HttpStatus.NOT_FOUND);

    }

    // Add other custom handlers

    // Generic HTTP exception handlers

    // 400

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        final List<String> errors = new ArrayList<String>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ApiErrorResponse apiError = new ApiErrorResponse
                .ApiErrorResponseBuilder()
                    .withStatus(HttpStatus.BAD_REQUEST)
                    .atTime(LocalDateTime.now(ZoneOffset.UTC))
                    .withMessage(ex.getLocalizedMessage())
                    .withDetails(errors.toString())
                    .build();
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        final List<String> errors = new ArrayList<String>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ApiErrorResponse apiError = new ApiErrorResponse
                .ApiErrorResponseBuilder()
                .withStatus(HttpStatus.BAD_REQUEST)
                .atTime(LocalDateTime.now(ZoneOffset.UTC))
                .withMessage(ex.getLocalizedMessage())
                .withDetails(errors.toString())
                .build();
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        final String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex.getRequiredType();
        final ApiErrorResponse apiError = new ApiErrorResponse
                .ApiErrorResponseBuilder()
                .withStatus(HttpStatus.BAD_REQUEST)
                .atTime(LocalDateTime.now(ZoneOffset.UTC))
                .withMessage(ex.getLocalizedMessage())
                .withDetails(error)
                .build();
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        final String error = ex.getRequestPartName() + " part is missing";
        final ApiErrorResponse apiError = new ApiErrorResponse
                .ApiErrorResponseBuilder()
                .withStatus(HttpStatus.BAD_REQUEST)
                .atTime(LocalDateTime.now(ZoneOffset.UTC))
                .withMessage(ex.getLocalizedMessage())
                .withDetails(error)
                .build();
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        final String error = ex.getParameterName() + " parameter is missing";
        final ApiErrorResponse apiError = new ApiErrorResponse
                .ApiErrorResponseBuilder()
                .withStatus(HttpStatus.BAD_REQUEST)
                .atTime(LocalDateTime.now(ZoneOffset.UTC))
                .withMessage(ex.getLocalizedMessage())
                .withDetails(error)
                .build();
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex, final WebRequest request) {
        logger.info(ex.getClass().getName());
        final String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
        final ApiErrorResponse apiError = new ApiErrorResponse
                .ApiErrorResponseBuilder()
                .withStatus(HttpStatus.BAD_REQUEST)
                .atTime(LocalDateTime.now(ZoneOffset.UTC))
                .withMessage(ex.getLocalizedMessage())
                .withDetails(error)
                .build();
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
        logger.info(ex.getClass().getName());
        final List<String> errors = new ArrayList<String>();
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
        }
        final ApiErrorResponse apiError = new ApiErrorResponse
                .ApiErrorResponseBuilder()
                .withStatus(HttpStatus.BAD_REQUEST)
                .atTime(LocalDateTime.now(ZoneOffset.UTC))
                .withMessage(ex.getLocalizedMessage())
                .withDetails(errors.toString())
                .build();
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 404

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        final ApiErrorResponse apiError = new ApiErrorResponse
                .ApiErrorResponseBuilder()
                .withStatus(HttpStatus.NOT_FOUND)
                .atTime(LocalDateTime.now(ZoneOffset.UTC))
                .withMessage(ex.getLocalizedMessage())
                .withDetails(error)
                .build();
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 405

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
        final ApiErrorResponse apiError = new ApiErrorResponse.ApiErrorResponseBuilder()
            .withStatus(HttpStatus.METHOD_NOT_ALLOWED)
            .atTime(LocalDateTime.now(ZoneOffset.UTC))
            .withMessage(ex.getLocalizedMessage())
            .withDetails(builder.toString())
            .build();
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 415

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));
        final ApiErrorResponse apiError = new ApiErrorResponse.ApiErrorResponseBuilder()
            .withStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
            .atTime(LocalDateTime.now(ZoneOffset.UTC))
            .withMessage(ex.getLocalizedMessage())
            .withDetails(builder.substring(0, builder.length() - 2))
            .build();
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 500

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
        logger.info(ex.getClass().getName());
        logger.error("error", ex);
        final ApiErrorResponse apiError = new ApiErrorResponse.ApiErrorResponseBuilder()
                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .atTime(LocalDateTime.now(ZoneOffset.UTC))
                .withMessage(ex.getLocalizedMessage())
                .withDetails("error occurred")
                .build();
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

}
