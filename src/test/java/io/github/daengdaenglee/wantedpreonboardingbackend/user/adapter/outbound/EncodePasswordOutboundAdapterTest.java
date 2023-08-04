package io.github.daengdaenglee.wantedpreonboardingbackend.user.adapter.outbound;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class EncodePasswordOutboundAdapterTest {

    private PasswordEncoder mockPasswordEncoder;

    private EncodePasswordOutboundAdapter encodePasswordOutboundAdapter;

    @BeforeEach
    void beforeEach() {
        this.mockPasswordEncoder = Mockito.mock(PasswordEncoder.class);
        this.encodePasswordOutboundAdapter = new EncodePasswordOutboundAdapter(this.mockPasswordEncoder);
    }

    @Test
    @DisplayName("입력값을 PasswordEncoder 에 그대로 전달한다.")
    void proxyInput() {
        var rawPassword = "rawPassword";

        this.encodePasswordOutboundAdapter.encode(rawPassword);

        var passedRawPasswordCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(this.mockPasswordEncoder)
                .encode(passedRawPasswordCaptor.capture());
        var passedRawPassword = passedRawPasswordCaptor.getValue();

        assertThat(passedRawPassword).isEqualTo(rawPassword);
    }

    @Test
    @DisplayName("PasswordEncoder 가 반환한 값을 그대로 반환한다..")
    void proxyOutput() {
        var rawPassword = "rawPassword";
        var encodedPassword = "encodedPassword";
        Mockito.when(this.mockPasswordEncoder.encode(rawPassword))
                .thenReturn(encodedPassword);

        var result = this.encodePasswordOutboundAdapter.encode(rawPassword);

        assertThat(result).isEqualTo(encodedPassword);
    }

}