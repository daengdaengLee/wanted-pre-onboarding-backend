package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound.SignUpInboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound.EncodePasswordOutboundPort;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound.UserRepository;
import io.github.daengdaenglee.wantedpreonboardingbackend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class SignUpServiceTest {

    private EncodePasswordOutboundPort mockEncodePasswordOutboundPort;

    private UserRepository mockUserRepository;

    private SignUpService signUpService;

    @BeforeEach
    void beforeEach() {
        this.mockEncodePasswordOutboundPort = Mockito.mock(EncodePasswordOutboundPort.class);
        this.mockUserRepository = Mockito.mock(UserRepository.class);
        this.signUpService = new SignUpService(
                this.mockEncodePasswordOutboundPort,
                this.mockUserRepository);
    }

    @Test
    @DisplayName("이메일 주소에 @ 가 없으면 에러를 반환한다.")
    void invalidEmail() {
        var invalidEmail = "example-email.com";

        var result = this.signUpService.signUp(
                new SignUpInboundPort.InputDto(invalidEmail, "12345678"));

        assertThat(result.isLeft()).isTrue();
        assertThat(result.left()).isEqualTo(SignUpInboundPort.ErrorCode.ILLEGAL_EMAIL_NO_AT);
    }

    @Test
    @DisplayName("이미 사용중인 이메일 주소면 에러를 반환한다.")
    void userNotExist() {
        var email = "example@email.com";
        var user = Mockito.mock(User.class);
        Mockito.when(this.mockUserRepository.findByEmail(email)).thenReturn(Optional.of(user));

        var result = this.signUpService.signUp(
                new SignUpInboundPort.InputDto(email, "12345678"));

        assertThat(result.isLeft()).isTrue();
        assertThat(result.left()).isEqualTo(SignUpInboundPort.ErrorCode.ILLEGAL_EMAIL_DUPLICATED);
    }

    @Test
    @DisplayName("비밀번호가 8자리보다 짧으면 에러를 반환한다.")
    void invalidPassword() {
        var invalidPassword = "1234";
        var email = "example@email.com";
        Mockito.when(this.mockUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        var result = this.signUpService.signUp(
                new SignUpInboundPort.InputDto(email, invalidPassword));

        assertThat(result.isLeft()).isTrue();
        assertThat(result.left()).isEqualTo(SignUpInboundPort.ErrorCode.ILLEGAL_PASSWORD_TOO_SHORT);
    }

    @Test
    @DisplayName("이메일과 인코딩한 비밀번호로 User 를 저장한다.")
    void saveEmailAndEncodedPassword() {
        var id = 1L;
        var email = "example@email.com";
        var password = "12345678";
        var encodedPassword = "encodedPassword12345678";
        var user = Mockito.mock(User.class);
        Mockito.when(user.id()).thenReturn(id);
        Mockito.when(user.email()).thenReturn(email);
        Mockito.when(this.mockUserRepository.findByEmail(email)).thenReturn(Optional.empty());
        Mockito.when(this.mockUserRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(this.mockEncodePasswordOutboundPort.encode(password)).thenReturn(encodedPassword);

        this.signUpService.signUp(
                new SignUpInboundPort.InputDto(email, password));

        var savedUserCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(this.mockUserRepository).save(savedUserCaptor.capture());
        var savedUser = savedUserCaptor.getValue();

        assertThat(savedUser.email()).isEqualTo(email);
        assertThat(savedUser.password()).isEqualTo(encodedPassword);
    }

    @Test
    @DisplayName("생성한 User 의 id, email 을 반환한다.")
    void success() {
        var id = 1L;
        var email = "example@email.com";
        var password = "12345678";
        var encodedPassword = "encodedPassword12345678";
        var user = Mockito.mock(User.class);
        Mockito.when(user.id()).thenReturn(id);
        Mockito.when(user.email()).thenReturn(email);
        Mockito.when(this.mockUserRepository.findByEmail(email)).thenReturn(Optional.empty());
        Mockito.when(this.mockUserRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(this.mockEncodePasswordOutboundPort.encode(password)).thenReturn(encodedPassword);

        var result = this.signUpService.signUp(
                new SignUpInboundPort.InputDto(email, password));

        assertThat(result.isRight()).isTrue();
        var dto = result.right();
        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.email()).isEqualTo(email);
    }

}