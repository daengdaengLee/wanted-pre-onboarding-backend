package io.github.daengdaenglee.wantedpreonboardingbackend.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordTest {

    @Test
    @DisplayName("비밀번호가 8자리보다 짧으면 에러를 반환한다.")
    void invalidPassword() {
        var invalidPassword = "1234";

        var passwordResult = Password.create(invalidPassword);

        assertThat(passwordResult.isLeft()).isTrue();
        assertThat(passwordResult.left()).isEqualTo(Password.ErrorCode.TOO_SHORT);
    }

    @Test
    @DisplayName("비밀번호에 문제가 없으면 Password 객체를 반환한다.")
    void validPassword() {
        var validPassword = "12345678";

        var passwordResult = Password.create(validPassword);

        assertThat(passwordResult.isRight()).isTrue();
        assertThat(passwordResult.right()).isInstanceOf(Password.class);
    }

    @Test
    @DisplayName("Password 객체의 password() getter 는 주입한 비밀번호를 그대로 반환한다.")
    void equalPassword() {
        var inputPassword = "12345678";

        var passwordResult = Password.create(inputPassword);
        var password = passwordResult.right();

        assertThat(password.password()).isEqualTo(inputPassword);
    }

}