package io.github.daengdaenglee.wantedpreonboardingbackend.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AuthTest {

    @Test
    @DisplayName("Auth 생성 메소드에 null 을 넣으면 빈 Optional 객체를 반환한다.")
    void nullAuthentication() {
        var authResult = Auth.create(null);

        assertThat(authResult.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Authentication 객체의 getPrincipal() 이 null 을 반환하면 빈 Optional 객체를 반환한다.")
    void invalidPrincipal1() {
        var authentication = new UsernamePasswordAuthenticationToken(null, null, null);

        var authResult = Auth.create(authentication);

        assertThat(authResult.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Authentication 객체의 getPrincipal() 이 Long 타입이 아닌 값을 반환하면 빈 Optional 객체를 반환한다.")
    void invalidPrincipal2() {
        var authentication = new UsernamePasswordAuthenticationToken("Long 아님", null, null);

        var authResult = Auth.create(authentication);

        assertThat(authResult.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("""
            Authentication 객체의 getPrincipal() 이 반환한 값을 그대로 담고있는
            Optional<Auth> 객체를 반환한다.
            """)
    void validPrincipal() {
        var inputId = 1L;
        var authentication = new UsernamePasswordAuthenticationToken(inputId, null, null);

        var authResult = Auth.create(authentication);

        assertThat(authResult.map(Auth::userId)).isEqualTo(Optional.of(inputId));
    }

}