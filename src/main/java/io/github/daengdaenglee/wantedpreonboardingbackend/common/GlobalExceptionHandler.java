package io.github.daengdaenglee.wantedpreonboardingbackend.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record OutputDto(String message) {
    }

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({SimpleApiException.class})
    public ResponseEntity<OutputDto> handleSimpleApiException(SimpleApiException ex) {
        return new ResponseEntity<>(new OutputDto(ex.message()), ex.httpStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<OutputDto> handleException(Exception ex) {
        this.logger.error("처리되지 않은 에러 : ", ex);
        return new ResponseEntity<>(new OutputDto(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
