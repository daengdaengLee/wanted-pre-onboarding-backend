package io.github.daengdaenglee.wantedpreonboardingbackend.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.io.InputStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void beforeEach() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("""
            SimpleApiException 이 발생하면
            SimpleApiException 의 httpStatus() getter, message() getter 가 반환한 값 그대로
            ResponseEntity 를 생성한다.
            """)
    void handleSimpleApiException() {
        var inputHttpStatus = HttpStatus.BAD_REQUEST;
        var inputMessage = "잘못된 요청입니다.";
        var simpleApiException = new SimpleApiException(inputHttpStatus, inputMessage);

        var responseEntity = this.globalExceptionHandler.handleSimpleApiException(simpleApiException);

        assertThat(responseEntity.getStatusCode().value())
                .isEqualTo(inputHttpStatus.value());
        assertThat(
                Optional.ofNullable(responseEntity.getBody())
                        .map(GlobalExceptionHandler.OutputDto::message)
                        .orElse(null))
                .isEqualTo(inputMessage);
    }

    @Test
    @DisplayName("""
            HttpMessageNotReadableException 이 발생하면
            400 HttpStatus 에 "잘못된 요청입니다." message 가 담긴
            ResponseEntity 를 생성한다.
            """)
    void handleHttpMessageNotReadableException() {
        class DummyHttpInputMessage implements HttpInputMessage {
            @Override
            public InputStream getBody() {
                return null;
            }

            @Override
            public HttpHeaders getHeaders() {
                return null;
            }
        }
        var ex = new HttpMessageNotReadableException("메세지", new DummyHttpInputMessage());

        var responseEntity = this.globalExceptionHandler.handleHttpMessageNotReadableException(ex);

        assertThat(responseEntity.getStatusCode().value())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(
                Optional.ofNullable(responseEntity.getBody())
                        .map(GlobalExceptionHandler.OutputDto::message)
                        .orElse(null))
                .isEqualTo("잘못된 요청입니다.");
    }

    @Test
    @DisplayName("""
            다른 Exception 이 발생하면
            500 HttpStatus 에 Exception 의 message() getter 가 반환한 값이 담긴
            ResponseEntity 를 생성한다.
            """)
    void handleException() {
        var inputMessage = "테스트 메세지";
        var ex = new RuntimeException(inputMessage);

        var responseEntity = this.globalExceptionHandler.handleException(ex);

        assertThat(responseEntity.getStatusCode().value())
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(
                Optional.ofNullable(responseEntity.getBody())
                        .map(GlobalExceptionHandler.OutputDto::message)
                        .orElse(null))
                .isEqualTo(inputMessage);
    }

}