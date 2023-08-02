package io.github.daengdaenglee.wantedpreonboardingbackend.user.adapter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

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

}
