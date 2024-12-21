package org.hplr.bootstrap.infrastructure.controller;

import lombok.extern.slf4j.Slf4j;
import org.hplr.library.exception.HPLRAccessDeniedException;
import org.hplr.library.exception.HPLRValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice(basePackages = {
        "org.hplr.user.infrastructure.controller",
        "org.hplr.game.infrastructure.controller",
        "org.hplr.tournament.infrastructure.controller"
}
)
public class RESTExceptionHandler
        extends ResponseEntityExceptionHandler {


    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleGenericException(final Exception ex ,final WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        log.error(ex.toString());
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value
            = { NoSuchElementException.class })
    protected ResponseEntity<Object> handleMissingElement(
            NoSuchElementException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        log.error("{}{}", ex, Arrays.toString(ex.getStackTrace()));
        log.error(bodyOfResponse);
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value
            = { HPLRValidationException.class })
    protected ResponseEntity<Object> handleIllegalState(
            HPLRValidationException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value
            = { NullPointerException.class })
    protected ResponseEntity<Object> handleNullPointerException(
            NullPointerException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value
            = { HPLRAccessDeniedException.class })
    protected ResponseEntity<Object> handleHPLRAccessDeniedException(
            HPLRAccessDeniedException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }
}