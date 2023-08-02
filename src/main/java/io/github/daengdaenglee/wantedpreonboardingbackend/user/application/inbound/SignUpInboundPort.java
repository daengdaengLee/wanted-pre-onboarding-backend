package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.common.Either;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.Email;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.Password;

public interface SignUpInboundPort {

    enum ErrorCode {

        ILLEGAL_EMAIL_NO_AT(Email.ErrorCode.NO_AT.message()),
        ILLEGAL_EMAIL_DUPLICATED("이미 사용 중인 이메일입니다."),
        ILLEGAL_PASSWORD_TOO_SHORT(Password.ErrorCode.TOO_SHORT.message());

        private final String message;

        ErrorCode(String message) {
            this.message = message;
        }

        public String message() {
            return this.message;
        }

    }

    record InputDto(String email, String password) {
    }

    Either<ErrorCode, UserOutputDto> signUp(InputDto inputDto);

}
