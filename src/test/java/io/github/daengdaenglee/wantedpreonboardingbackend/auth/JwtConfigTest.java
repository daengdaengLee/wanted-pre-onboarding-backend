package io.github.daengdaenglee.wantedpreonboardingbackend.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtConfigTest {

    private JwtConfig jwtConfig;

    @BeforeEach
    void beforeEach() {
        jwtConfig = new JwtConfig();
        jwtConfig.setHeader("Authentication");
        jwtConfig.setPrefix("Bearer ");
        jwtConfig.setSecret("this-is-secret");
        jwtConfig.setValidityInSec(604800);
    }

    @Test
    @DisplayName("header 가 null 이면 afterPropertiesSet() 단계에서 에러를 던진다.")
    void nullHeader() {
        jwtConfig.setHeader(null);

        assertThatThrownBy(() -> jwtConfig.afterPropertiesSet()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("header 가 빈 문자열이면 afterPropertiesSet() 단계에서 에러를 던진다.")
    void emptyHeader() {
        jwtConfig.setHeader("");

        assertThatThrownBy(() -> jwtConfig.afterPropertiesSet()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("header 가 공백만 있는 문자열이면 afterPropertiesSet() 단계에서 에러를 던진다.")
    void blankHeader() {
        jwtConfig.setHeader("   ");

        assertThatThrownBy(() -> jwtConfig.afterPropertiesSet()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("prefix 가 null 이면 afterPropertiesSet() 단계에서 에러를 던진다.")
    void nullPrefix() {
        jwtConfig.setPrefix(null);

        assertThatThrownBy(() -> jwtConfig.afterPropertiesSet()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("secret 이 null 이면 afterPropertiesSet() 단계에서 에러를 던진다.")
    void nullSecret() {
        jwtConfig.setSecret(null);

        assertThatThrownBy(() -> jwtConfig.afterPropertiesSet()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("secret 가 빈 문자열이면 afterPropertiesSet() 단계에서 에러를 던진다.")
    void emptySecret() {
        jwtConfig.setSecret("");

        assertThatThrownBy(() -> jwtConfig.afterPropertiesSet()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("secret 가 공백만 있는 문자열이면 afterPropertiesSet() 단계에서 에러를 던진다.")
    void blankSecret() {
        jwtConfig.setSecret("   ");

        assertThatThrownBy(() -> jwtConfig.afterPropertiesSet()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("validityInSec 이 null 이면 afterPropertiesSet() 단계에서 에러를 던진다.")
    void nullValidityInSec() {
        jwtConfig.setValidityInSec(null);

        assertThatThrownBy(() -> jwtConfig.afterPropertiesSet()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("validityInSec 이 600 이하이면 afterPropertiesSet() 단계에서 에러를 던진다.")
    void smallValidityInSec() {
        jwtConfig.setValidityInSec(60);

        assertThatThrownBy(() -> jwtConfig.afterPropertiesSet()).isInstanceOf(IllegalStateException.class);
    }

}