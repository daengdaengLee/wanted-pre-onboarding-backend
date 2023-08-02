package io.github.daengdaenglee.wantedpreonboardingbackend.user.application.service;

import io.github.daengdaenglee.wantedpreonboardingbackend.user.application.inbound.SignInInboundPort;
import org.springframework.stereotype.Service;

@Service
public class SignInService implements SignInInboundPort {

    @Override
    public OutputDto signIn(InputDto inputdto) {
        throw new RuntimeException("not implemented");
    }

}
