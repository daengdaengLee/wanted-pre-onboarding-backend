package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound.SignInInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound.CheckPasswordOutboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound.EncodeJwtOutboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound.UserRepository;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class SignInServiceTest {

    @Mock
    private CheckPasswordOutboundPort mockCheckPasswordOutboundPort;

    @Mock
    private EncodeJwtOutboundPort mockEncodeJwtOutboundPort;

    @Mock
    private UserRepository mockUserRepository;

    private SignInService signInService;


    @BeforeEach
    void beforeEach() {
        signInService = new SignInService(
                this.mockCheckPasswordOutboundPort,
                this.mockEncodeJwtOutboundPort,
                this.mockUserRepository);
    }

    @Test
    @DisplayName("이메일 주소에 @ 가 없으면 에러를 반환한다.")
    void invalidEmail() {
        var invalidEmail = "example-email.com";

        var result = this.signInService.signIn(
                new SignInInboundPort.InputDto(invalidEmail, "12345678"));

        assertThat(result.isLeft()).isTrue();
        assertThat(result.left()).isEqualTo(SignInInboundPort.ErrorCode.ILLEGAL_EMAIL_NO_AT);
    }

    @Test
    @DisplayName("이메일 주소를 가진 유저를 찾지 못하면 에러를 반환한다.")
    void userNotExist() {
        var email = "example@email.com";
        Mockito.when(this.mockUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        var result = this.signInService.signIn(
                new SignInInboundPort.InputDto(email, "12345678"));

        assertThat(result.isLeft()).isTrue();
        assertThat(result.left()).isEqualTo(SignInInboundPort.ErrorCode.ILLEGAL_EMAIL_NOT_SIGNED_UP);
    }

    @Test
    @DisplayName("비밀번호가 8자리보다 짧으면 에러를 반환한다.")
    void invalidPassword() {
        var invalidPassword = "1234";
        var email = "example@email.com";
        var user = Mockito.mock(User.class);
        Mockito.when(this.mockUserRepository.findByEmail(email)).thenReturn(Optional.of(user));

        var result = this.signInService.signIn(
                new SignInInboundPort.InputDto(email, invalidPassword));

        assertThat(result.isLeft()).isTrue();
        assertThat(result.left()).isEqualTo(SignInInboundPort.ErrorCode.ILLEGAL_PASSWORD_TOO_SHORT);
    }

    @Test
    @DisplayName("비밀번호가 맞지 않으면 에러를 반환한다.")
    void unmatchedPassword() {
        var password = "12345678";
        var encodedPassword = "encodedPassword12345678";
        var email = "example@email.com";
        var user = Mockito.mock(User.class);
        Mockito.when(user.password()).thenReturn(encodedPassword);
        Mockito.when(this.mockUserRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(this.mockCheckPasswordOutboundPort.check(password, encodedPassword)).thenReturn(false);

        var result = this.signInService.signIn(
                new SignInInboundPort.InputDto(email, password));

        assertThat(result.isLeft()).isTrue();
        assertThat(result.left()).isEqualTo(SignInInboundPort.ErrorCode.ILLEGAL_PASSWORD_WRONG);
    }

    @Test
    @DisplayName("인증에 성공하면 id, email 과 함께 생성한 token 을 반환한다.")
    void success() {
        var id = 1L;
        var password = "12345678";
        var encodedPassword = "encodedPassword12345678";
        var email = "example@email.com";
        var token = "FakeJWT";
        var user = Mockito.mock(User.class);
        Mockito.when(user.id()).thenReturn(id);
        Mockito.when(user.password()).thenReturn(encodedPassword);
        Mockito.when(user.email()).thenReturn(email);
        Mockito.when(this.mockUserRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(this.mockCheckPasswordOutboundPort.check(password, encodedPassword)).thenReturn(true);
        Mockito.when(this.mockEncodeJwtOutboundPort.encode(Mockito.any(EncodeJwtOutboundPort.InputDto.class)))
                .thenReturn(token);

        var result = this.signInService.signIn(
                new SignInInboundPort.InputDto(email, password));

        assertThat(result.isRight()).isTrue();
        var dto = result.right();
        assertThat(dto.user().id()).isEqualTo(id);
        assertThat(dto.user().email()).isEqualTo(email);
        assertThat(dto.token()).isEqualTo(token);
    }

}