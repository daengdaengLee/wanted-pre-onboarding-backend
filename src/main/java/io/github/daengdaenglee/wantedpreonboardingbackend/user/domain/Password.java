package io.github.daengdaenglee.wantedpreonboardingbackend.user.domain;

import io.github.daengdaenglee.wantedpreonboardingbackend.common.Either;

public class Password {

    public enum ErrorCode {
        TOO_SHORT("비밀번호는 8자리 이상이어야 합니다.");

        private final String message;

        ErrorCode(String message) {
            this.message = message;
        }

        public String message() {
            return this.message;
        }

    }

    public static Either<ErrorCode, Password> create(String password) {
        if (password.length() >= 8) {
            return new Either.Right<>(new Password(password));
        }

        return new Either.Left<>(ErrorCode.TOO_SHORT);
    }

    private final String password;

    private Password(String password) {
        this.password = password;
    }

    public String password() {
        return this.password;
    }

}
