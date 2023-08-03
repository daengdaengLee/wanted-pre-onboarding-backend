package io.github.daengdaenglee.wantedpreonboardingbackend.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    @DisplayName("""
            이메일과 암호화한 비밀번호로 User 객체를 만들면
            email() getter, password() getter 에서 그대로 반환한다.
            """)
    void createByEmailAndPassword() {
        var inputEmail = "example@emai.com";
        var inputPassword = "##encoded-password##";
        var email = Email.create(inputEmail).right();
        var encodedPassword = new EncodedPassword(inputPassword);

        var user = new User(email, encodedPassword);

        assertThat(user.email()).isEqualTo(inputEmail);
        assertThat(user.password()).isEqualTo(inputPassword);
    }

    @Test
    @DisplayName("아아디로 User 객체를 만들면 id() getter 에서 그대로 반환한다.")
    void createById() {
        var inputId = 1L;

        var user = new User(inputId);

        assertThat(user.id()).isEqualTo(inputId);
    }

}