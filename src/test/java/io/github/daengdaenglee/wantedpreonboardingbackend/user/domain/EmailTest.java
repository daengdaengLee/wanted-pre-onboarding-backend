package io.github.daengdaenglee.wantedpreonboardingbackend.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTest {

    @Test
    @DisplayName("이메일 주소에 @ 가 없으면 에러를 반환한다.")
    void invalidEmail() {
        var invalidEmailAddress = "example-email.com";

        var emailResult = Email.create(invalidEmailAddress);

        assertThat(emailResult.isLeft()).isTrue();
        assertThat(emailResult.left()).isEqualTo(Email.ErrorCode.NO_AT);
    }

    @Test
    @DisplayName("이메일 주소에 문제가 없으면 Email 객체를 반환한다.")
    void validEmail() {
        var validEmailAddress = "example@email.com";

        var emailResult = Email.create(validEmailAddress);

        assertThat(emailResult.isRight()).isTrue();
        assertThat(emailResult.right()).isInstanceOf(Email.class);
    }

    @Test
    @DisplayName("Email 객체의 email() getter 는 주입한 이메일 주소를 그대로 반환한다.")
    void equalEmailAddress() {
        var emailAddress = "example@email.com";

        var emailResult = Email.create(emailAddress);
        var email = emailResult.right();

        assertThat(email.email()).isEqualTo(emailAddress);
    }

}