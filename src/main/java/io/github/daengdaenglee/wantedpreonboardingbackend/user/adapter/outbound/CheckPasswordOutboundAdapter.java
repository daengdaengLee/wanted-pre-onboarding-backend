package io.github.daengdaenglee.wantedpreonboardingbackend.user.adapter.outbound;

import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.outbound.CheckPasswordOutboundPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CheckPasswordOutboundAdapter implements CheckPasswordOutboundPort {

    private final PasswordEncoder passwordEncoder;

    public CheckPasswordOutboundAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean check(String rawPassword, String encodedPassword) {
        return this.passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
