package io.github.daengdaenglee.wantedpreonboardingbackend.user.domain;

import io.github.daengdaenglee.wantedpreonboardingbackend.common.Either;

public class Email {

    public enum ErrorCode {

        NO_AT("이메일 주소에는 \"@\"가 반드시 들어가야 합니다.");

        private final String message;

        ErrorCode(String message) {
            this.message = message;
        }

        public String message() {
            return this.message;
        }

    }

    public static Either<ErrorCode, Email> create(String email) {
        if (email.contains("@")) {
            return new Either.Right<>(new Email(email));
        }

        return new Either.Left<>(ErrorCode.NO_AT);
    }

    private final String email;

    private Email(String email) {
        this.email = email;
    }

    public String email() {
        return this.email;
    }

}
