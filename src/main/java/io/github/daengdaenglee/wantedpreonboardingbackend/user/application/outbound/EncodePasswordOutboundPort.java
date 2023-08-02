package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound;

public interface EncodePasswordOutboundPort {

    String encode(String rawPassword);

}
