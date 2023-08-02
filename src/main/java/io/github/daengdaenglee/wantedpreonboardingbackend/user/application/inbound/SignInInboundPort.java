package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.common.Either;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.Email;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.Password;

public interface SignInInboundPort {

    enum ErrorCode {

        ILLEGAL_EMAIL_NO_AT(Email.ErrorCode.NO_AT.message()),
        ILLEGAL_EMAIL_NOT_SIGNED_UP("가입하지 않은 이메일 주소입니다."),
        ILLEGAL_PASSWORD_TOO_SHORT(Password.ErrorCode.TOO_SHORT.message()),
        ILLEGAL_PASSWORD_WRONG("잘못된 비밀번호입니다.");

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

    record OutputDto(UserOutputDto user, String token) {
    }

    Either<ErrorCode, OutputDto> signIn(InputDto inputdto);

}
