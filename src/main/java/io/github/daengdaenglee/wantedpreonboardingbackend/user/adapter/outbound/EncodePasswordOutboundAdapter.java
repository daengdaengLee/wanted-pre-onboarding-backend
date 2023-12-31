package io.github.daengdaenglee.wantedpreonboardingbackend.user.adapter.outbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound.EncodePasswordOutboundPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncodePasswordOutboundAdapter implements EncodePasswordOutboundPort {

    private final PasswordEncoder passwordEncoder;

    public EncodePasswordOutboundAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String rawPassword) {
        return this.passwordEncoder.encode(rawPassword);
    }

}
