package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound;

public interface CheckPasswordOutboundPort {

    boolean check(String rawPassword, String encodedPassword);

}
