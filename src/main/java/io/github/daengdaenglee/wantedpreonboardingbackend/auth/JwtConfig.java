package io.github.daengdaenglee.wantedpreonboardingbackend.auth;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtConfig implements InitializingBean {

    private String header;

    private String prefix;

    private String secret;

    private Integer validityInSec;

    public String header() {
        return this.header;
    }

    public String prefix() {
        return this.prefix;
    }

    public String secret() {
        return this.secret;
    }

    public Integer validityInSec() {
        return this.validityInSec;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setValidityInSec(Integer validityInSec) {
        this.validityInSec = validityInSec;
    }

    @Override
    public void afterPropertiesSet() {
        this.validate();
    }

    private void validate() {
        this.validateHeader();
        this.validatePrefix();
        this.validateSecret();
        this.validateValidityInSec();
    }

    private void validateHeader() {
        var header = this.header();
        if (header == null || header.isEmpty() || header.isBlank()) {
            throw new IllegalStateException("header 설정값이 없습니다.");
        }
    }

    private void validatePrefix() {
        var prefix = this.prefix();
        if (prefix == null) {
            throw new IllegalStateException("prefix 설정값이 없습니다.");
        }
    }

    private void validateSecret() {
        var secret = this.secret();
        if (secret == null || secret.isEmpty() || secret.isBlank()) {
            throw new IllegalStateException("secret 설정값이 없습니다.");
        }
    }

    private void validateValidityInSec() {
        var validityInSec = this.validityInSec();
        if (validityInSec == null) {
            throw new IllegalStateException("validityInSec 설정값이 없습니다.");
        }
        if (validityInSec <= 10 * 60) {
            throw new IllegalStateException("validityInSec 설정값이 10분 이하입니다.");
        }
    }

}
