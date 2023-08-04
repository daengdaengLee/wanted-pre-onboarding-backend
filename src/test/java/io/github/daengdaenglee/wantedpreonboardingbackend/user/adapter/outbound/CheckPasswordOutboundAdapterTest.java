package io.github.daengdaenglee.wantedpreonboardingbackend.user.adapter.outbound;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class CheckPasswordOutboundAdapterTest {

    private PasswordEncoder mockPasswordEncoder;

    private CheckPasswordOutboundAdapter checkPasswordOutboundAdapter;

    @BeforeEach
    void beforeEach() {
        this.mockPasswordEncoder = Mockito.mock(PasswordEncoder.class);
        this.checkPasswordOutboundAdapter = new CheckPasswordOutboundAdapter(this.mockPasswordEncoder);
    }

    @Test
    @DisplayName("입력값을 PasswordEncoder 에 그대로 전달한다.")
    void proxyInput() {
        var rawPassword = "rawPassword";
        var encodedPassword = "encodedPassword";

        this.checkPasswordOutboundAdapter.check(rawPassword, encodedPassword);

        var passedRawPasswordCaptor = ArgumentCaptor.forClass(String.class);
        var passedEncodedPasswordCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(this.mockPasswordEncoder)
                .matches(passedRawPasswordCaptor.capture(), passedEncodedPasswordCaptor.capture());
        var passedRawPassword = passedRawPasswordCaptor.getValue();
        var passedEncodedPassword = passedEncodedPasswordCaptor.getValue();

        assertThat(passedRawPassword).isEqualTo(rawPassword);
        assertThat(passedEncodedPassword).isEqualTo(encodedPassword);
    }

    @Test
    @DisplayName("PasswordEncoder 가 true 를 반환하면 true 를 반환한다.")
    void proxyOutputTrue() {
        var rawPassword = "rawPassword";
        var encodedPassword = "encodedPassword";
        Mockito.when(this.mockPasswordEncoder.matches(rawPassword, encodedPassword))
                .thenReturn(true);

        var result = this.checkPasswordOutboundAdapter.check(rawPassword, encodedPassword);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("PasswordEncoder 가 false 를 반환하면 false 를 반환한다.")
    void proxyOutputFalse() {
        var rawPassword = "rawPassword";
        var encodedPassword = "encodedPassword";
        Mockito.when(this.mockPasswordEncoder.matches(rawPassword, encodedPassword))
                .thenReturn(false);

        var result = this.checkPasswordOutboundAdapter.check(rawPassword, encodedPassword);

        assertThat(result).isFalse();
    }

}