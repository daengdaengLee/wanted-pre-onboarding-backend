package io.github.daengdaenglee.wantedpreonboardingbackend.common;

import org.springframework.http.HttpStatus;

public class SimpleApiException extends RuntimeException {

    private final HttpStatus httpStatus;

    private final String message;

    public SimpleApiException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus httpStatus() {
        return this.httpStatus;
    }

    public String message() {
        return this.message;
    }

}
