package io.github.daengdaenglee.wantedpreonboardingbackend.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleApiExceptionTest {

    @Test
    @DisplayName("""
            HttpStatus, 메시지로 SimpleApiException 객체를 만들면
            httpStatus() getter, message() getter 에서 그대로 반환한다.
            """)
    void createByHttpStatusAndMessage() {
        var inputHttpStatus = HttpStatus.UNAUTHORIZED;
        var inputMessage = "로그인이 필요합니다.";

        var simpleApiException = new SimpleApiException(inputHttpStatus, inputMessage);

        assertThat(simpleApiException.httpStatus()).isEqualTo(inputHttpStatus);
        assertThat(simpleApiException.message()).isEqualTo(inputMessage);
    }

}